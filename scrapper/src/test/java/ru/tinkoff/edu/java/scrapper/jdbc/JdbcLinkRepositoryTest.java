package ru.tinkoff.edu.java.scrapper.jdbc;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.enums.LinkQuery;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import util.JdbcIntegrationEnvironment;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@Rollback
public class JdbcLinkRepositoryTest extends JdbcIntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcLinkRepository linkRepository;

    @Test
    public void save__linkUrlDoesntExistInDb_addedLink() {
        // given
        var link = new Link().setUrl("https://meta.com");

        // when
        var savedLink = linkRepository.save(link);

        // then
        Link realSavedLink = jdbcTemplate.queryForObject(LinkQuery.SELECT_BY_URL.query(),
                new BeanPropertyRowMapper<>(Link.class), link.getUrl());

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

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.save(link));
    }

    @Sql("/sql/fill_in_chat_link_pairs.sql")
    @ParameterizedTest
    @ValueSource(longs = {2222, 2223})
    public void removeById__LinkIsBeingTrackedInChat_throwException(long linkId) {
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.removeById(linkId));
    }

    @Test
    @Sql("/sql/fill_in_links.sql")
    public void removeById__LinkIsNotBeingTrackedInChat_removedLink() {
        // given
        var linkId = linkRepository.findAll().get(0).getId();

        // when
        var ifRemoved = linkRepository.removeById(linkId);

        // then
        List<Link> removedLink = jdbcTemplate.query(LinkQuery.SELECT_BY_ID.query(),
                new BeanPropertyRowMapper<>(Link.class), linkId);
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
        List<Link> realLinks = jdbcTemplate.query(LinkQuery.SELECT_ALL.query(),
                new BeanPropertyRowMapper<>(Link.class));
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
        // when
        List<Link> links = linkRepository.findAllByChat(new Chat().setId(chatId));

        // then
        List<Link> realLinks = jdbcTemplate.query(LinkQuery.SELECT_BY_CHAT.query(),
                new BeanPropertyRowMapper<>(Link.class), chatId);

        assertThat(links).hasSameSizeAs(realLinks);
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void addToChat__chatAlreadyHasLink_returnFalse() {
        // given
        var chat = new Chat().setId(1L);
        var link = new Link().setId(2222L);

        // when
        Boolean ifAdded = linkRepository.addToChat(chat, link);

        // then
        assertThat(ifAdded).isFalse();
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void addToChat__chatDoesntHaveLink_addedLinkToChat() {
        // given
        var chat = new Chat().setId(1L);
        var link = new Link().setId(2224L);

        // when
        Boolean ifAdded = linkRepository.addToChat(chat, link);

        // then
        Boolean existsInChat = jdbcTemplate.queryForObject(LinkQuery.EXISTS_IN_CHAT.query(),
                Boolean.class, chat.getId(), link.getId());
        assertAll(
                () -> assertThat(ifAdded).isTrue(),
                () -> assertThat(existsInChat).isTrue()
        );
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeFromChat__chatHaveLink_returnTrue() {
        // given
        var chat = new Chat().setId(1L);
        var link = new Link().setId(2222L);

        // when
        Boolean ifRemoved = linkRepository.removeFromChat(chat, link);

        // then
        Boolean existsInChat = jdbcTemplate.queryForObject(LinkQuery.EXISTS_IN_CHAT.query(),
                Boolean.class, chat.getId(), link.getId());
        assertAll(
                () -> assertThat(ifRemoved).isTrue(),
                () -> assertThat(existsInChat).isFalse()
        );
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeFromChat__chatDoesntHaveLink_returnFalse() {
        // given
        var chat = new Chat().setId(1L);
        var link = new Link().setId(2224L);

        // when
        Boolean ifRemoved = linkRepository.removeFromChat(chat, link);

        // then
        assertThat(ifRemoved).isFalse();
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
        List<Link> realLinks = jdbcTemplate.query(LinkQuery.SELECT_LEAST_RECENTLY_UPDATED.query(),
                new BeanPropertyRowMapper<>(Link.class), dateTime);

        assertThat(links).hasSameSizeAs(realLinks);
    }

}
