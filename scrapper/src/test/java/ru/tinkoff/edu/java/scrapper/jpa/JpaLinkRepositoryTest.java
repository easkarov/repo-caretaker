package ru.tinkoff.edu.java.scrapper.jpa;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.enums.LinkQuery;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import util.JpaIntegrationEnvironment;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@Rollback
public class JpaLinkRepositoryTest extends JpaIntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManager entityManager;

    @Autowired
    JpaLinkRepository linkRepository;

    @Test
    public void save__linkUrlDoesntExistInDb_addedLink() {
        // given
        var link = new Link().setUrl("https://meta.com");

        // when
        var savedLink = linkRepository.save(link);

        // then
        entityManager.flush();

        var query = entityManager.createQuery("SELECT l FROM Link l WHERE l.url = :url", Link.class);
        query.setParameter("url", link.getUrl());
        var realSavedLink = query.getSingleResult();

        assertThat(realSavedLink).isNotNull();
        assertAll(
                () -> assertThat(savedLink.getUrl()).isEqualTo(realSavedLink.getUrl()),
                () -> assertThat(savedLink.getId()).isEqualTo(realSavedLink.getId()),
                () -> assertThat(savedLink.getUpdateData()).isEqualTo(realSavedLink.getUpdateData())
        );
    }

    @Test
    @Sql({"/sql/fill_in_links.sql"})
    public void save__linkUrlAlreadyExistsInDb_throwException() {
        // given
        var link = new Link().setUrl("https://stackoverflow.com/7777777");
        linkRepository.save(link);

        // when, then
        assertThrows(PersistenceException.class, () -> entityManager.flush());
    }

    @Sql("/sql/fill_in_chat_link_pairs.sql")
    @ParameterizedTest
    @ValueSource(longs = {2222, 2223})
    public void removeById__linkIsBeingTrackedInChat_throwException(long linkId) {
        // when
        linkRepository.removeById(linkId);

        // then
        assertThrows(PersistenceException.class, () -> entityManager.flush());
    }

    @Test
    @Sql("/sql/fill_in_links.sql")
    public void removeById__LinkIsNotBeingTrackedInChat_removedLink() {
        // given
        var linkId = linkRepository.findAll().get(0).getId();

        // when
        var ifRemoved = linkRepository.removeById(linkId);
        entityManager.flush();

        // then
        var query = entityManager.createQuery("SELECT l FROM Link l WHERE l.id = :id", Link.class);
        query.setParameter("id", linkId);
        var removedLink = query.getResultList();

        assertAll(
                () -> assertThat(ifRemoved).isTrue(),
                () -> assertThat(removedLink).hasSize(0)
        );
    }

    @Test
    @Sql("/sql/fill_in_links.sql")
    public void findAll__dbHasSomeLinks_notEmptyLinkList() {
        // when
        List<Link> links = linkRepository.findAll();

        // then
        List<Link> realLinks = entityManager.createQuery("FROM Link", Link.class).getResultList();

        assertThat(links).hasSameSizeAs(realLinks);
    }

    @Test
    public void findAll__dbHasNoLinks_emptyLinkList() {
        // when
        List<Link> links = linkRepository.findAll();

        // then
        assertThat(links).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void findAllByChat__chatHasSomeLinks_correctLinkSizeList(long chatId) {
        var chat = entityManager.find(Chat.class, chatId);

        // when
        List<Link> links = linkRepository.findAllByChat(chat);

        // then
        var query = entityManager.createQuery("SELECT l FROM Link l JOIN FETCH l.chats c WHERE c.id = :id", Link.class);
        query.setParameter("id", chatId);
        List<Link> realLinks = query.getResultList();

        assertThat(links).hasSameSizeAs(realLinks);
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void addToChat__chatAlreadyHasLink_returnFalse() {
        // given
        var chat = entityManager.find(Chat.class, 1L);
        var link = entityManager.find(Link.class, 2222L);

        // when
        Boolean ifAdded = linkRepository.addToChat(chat, link);
        entityManager.flush();

        // then
        assertAll(
                () -> assertThat(ifAdded).isFalse(),
                () -> assertThat(chat.getLinks()).contains(link),
                () -> assertThat(link.getChats()).contains(chat)
        );
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void addToChat__chatDoesntHaveLink_addedLinkToChat() {
        // given
        var chat = entityManager.find(Chat.class, 1L);
        var link = entityManager.find(Link.class, 2224L);

        // when
        Boolean ifAdded = linkRepository.addToChat(chat, link);
        entityManager.flush();

        // then
        assertAll(
                () -> assertThat(ifAdded).isTrue(),
                () -> assertThat(chat.getLinks()).contains(link),
                () -> assertThat(link.getChats()).contains(chat)
        );
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeFromChat__chatHaveLink_returnTrue() {
        // given

        var chat = entityManager.find(Chat.class, 1L);
        var link = entityManager.find(Link.class, 2222L);

        // when
        Boolean ifRemoved = linkRepository.removeFromChat(chat, link);
        entityManager.flush();

        // then
        assertAll(
                () -> assertThat(ifRemoved).isTrue(),
                () -> assertThat(chat.getLinks()).doesNotContain(link),
                () -> assertThat(link.getChats()).doesNotContain(chat)
        );
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeFromChat__chatDoesntHaveLink_returnFalse() {
        // given
        var chat = entityManager.find(Chat.class, 1L);
        var link = entityManager.find(Link.class, 2224L);

        // when
        Boolean ifRemoved = linkRepository.removeFromChat(chat, link);
        entityManager.flush();

        // then
        assertAll(
                () -> assertThat(ifRemoved).isFalse(),
                () -> assertThat(chat.getLinks()).doesNotContain(link),
                () -> assertThat(link.getChats()).doesNotContain(chat)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-01-01T00:00:00Z", "2022-01-01T00:00:00Z"})
    @Sql("/sql/fill_in_links.sql")
    public void findLeastRecentlyUpdated__dbHasSomeOldLinks_correctLinkListSize(String dateString) {
        // given
        var dateTime = OffsetDateTime.parse(dateString);

        // when
        List<Link> links = linkRepository.findLeastRecentlyUpdated(dateTime);

        // then
        var query = entityManager.createQuery("SELECT l FROM Link l WHERE l.updatedAt < :date", Link.class);
        query.setParameter("date", dateTime);
        List<Link> realLinks = query.getResultList();

        assertThat(links).hasSameSizeAs(realLinks);
    }

}
