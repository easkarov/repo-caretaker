package ru.tinkoff.edu.java.scrapper;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import util.IntegrationEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    LinkRepository linkRepository;

    @Test
    @Transactional
    public void save__LinkDoesntExistInDb_addedLink() {
        // given
        var link = new Link().setUrl("http://stackoverflow.com/123123");

        // when
        var addedLink = linkRepository.save(link);

        // Корректнее ли было бы здесь проверять действительно была добавлена ссылка или нет?
        // Или достаточно проверять именно контракт метода (то, что он возаращает)?

        assertThat(addedLink.getUrl()).isEqualTo(link.getUrl());
    }

    @Test
    @Transactional
    public void save__LinkUrlAlreadyExistsInDb_throwException() {
        // given
        var link = new Link().setUrl("http://stackoverflow.com/123123");
        linkRepository.save(link);

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.save(link));
    }

    @Test
    @Transactional
    public void removeById__LinkIsBeingTrackedInChat_throwException() {
        // given
        var link = new Link().setId(123L).setUrl("http://stackoverflow.com/123123");
        var chatId = 777L;

//        var addedLink = linkRepository.save(link);
        jdbcTemplate.update("INSERT INTO link(id, url) VALUES(?, ?)", link.getId(), link.getUrl());
        jdbcTemplate.update("INSERT INTO chat VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", chatId, link.getId());

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.removeById(link.getId()));
    }

    @Test
    @Transactional
    public void removeById__LinkIsNotBeingTrackedInChat_oneRemovedLink() {
        // given
        var link = new Link().setId(123L).setUrl("http://stackoverflow.com/123123");
        jdbcTemplate.update("INSERT INTO link(id, url) VALUES(?, ?)", link.getId(), link.getUrl());

        // when

        // Как тут корректнее тестировать? То, что linkRepository.removeById
        // возвращает true - удаление прошло успешно (таков контракт метода),
        // либо же проверять не контракт а смотреть по существу с помощью jdbcTemplate,
        // произошло ли удаление

        // 1)
        assertThat(linkRepository.removeById(link.getId())).isTrue();

        // 2)
        Boolean ifRemoved = jdbcTemplate.queryForObject("SELECT COUNT(*) = 0 FROM link WHERE id = ?",
                Boolean.class, link.getId());
        assertThat(ifRemoved).isTrue();
    }

    @Test
    @Transactional
    @Sql("/sql/fill_in_links.sql") // в целом удобно использовать авто SQL скрипты, но насколько оправдано?
    public void findAll__SomeLinksInDb_listOfLinks() {
        // when
        List<Link> links = linkRepository.findAll();

        // соответственно, есть ли тут смысл проверять что в List<Link> links лежат действительно те линки,
        // которые есть в БД или достаточно проверить размер возвращаемого списка ссылок?

        // then
        assertThat(links).hasSize(2);

    }

    @Test
    @Transactional
    public void findAll__NoLinksInDb_emptyList() {
        // when
        List<Link> links = linkRepository.findAll();

        // then
        assertThat(links).isEmpty();

    }
}
