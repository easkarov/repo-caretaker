package ru.tinkoff.edu.java.scrapper;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import util.IntegrationEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    LinkRepository linkRepository;

//    @Test
//    @Transactional
//    public void addLink__LinkDoesntExistInDb_addedLink() {
//        // given
//        var link = new Link()
//                .setId(123L)
//                .setUrl("http://stackoverflow.com/123123");
//
//        // when
//        linkRepository.save(link);
//
//        // then
//        Link addedLink = jdbcTemplate.queryForObject("SELECT * FROM link WHERE id = ?",
//                new BeanPropertyRowMapper<>(Link.class),
//                link.getId());
//
//        assertAll(
//                () -> assertThat(addedLink).isNotNull(),
//                () -> assertThat(addedLink.getUrl()).isEqualTo(link.getUrl()),
//                () -> assertThat(addedLink.getId()).isEqualTo(link.getId())
//        );
//    }
//
//    @Test
//    @Transactional
//    public void addLink__LinkUrlAlreadyExistsInDb_throwException() {
//        // given
//        var link = new Link().setUrl("http://stackoverflow.com/123123");
//        linkRepository.save(link);
//
//        // when, then
//        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.save(link));
//    }

//    @Test
//    @Transactional
//    public void removeLink__LinkIsBeingTrackedInChat_throwException() {
//        // given
//        var link = new Link()
//                .setUrl("http://stackoverflow.com/123123");
//        var chatId = 777L;
//
//        var addedLink = linkRepository.save(link);
//        jdbcTemplate.update("INSERT INTO chat VALUES (?)", chatId);
//        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", chatId, addedLink.getId());
//
//        // when, then
//        assertThrows(DataIntegrityViolationException.class, () -> linkRepository.removeById(addedLink.getId()));
//    }
//
//    @Test
//    @Transactional
//    public void removeLink__LinkIsNotBeingTrackedInChat_oneRemovedLink() {
//        // given
//        var link = new Link().setId(123L).setUrl("http://stackoverflow.com/123123");
//        linkRepository.save(link);
//
//        // when
//        linkRepository.removeById(link.getId());
//
//        // then
//        Boolean ifRemoved = jdbcTemplate.queryForObject("SELECT COUNT(*) = 0 FROM link WHERE id = ?",
//                Boolean.class, link.getId());
//
//        assertThat(ifRemoved).isTrue();
//    }

    @Test
    @Transactional
    @Sql("classpath:sql/fill_in_links.sql")
    public void findAll__listOfLinks() {
        // given
//        var linksToAdd = List.of(
//                new Link().setUrl("http://stackoverflow/444"),
//                new Link().setUrl("http://github.com/gram3r/java")
//        );
//        linksToAdd.forEach(linkRepository::save);

        // when
        List<Link> links = linkRepository.findAll();

        // then
        assertThat(links).hasSize(2);

    }

    @Test
    @Transactional
    public void findAll__emptyList() {
        // given
//        var linksToAdd = List.of(
//                new Link().setUrl("http://stackoverflow/444"),
//                new Link().setUrl("http://github.com/gram3r/java")
//        );
//        linksToAdd.forEach(linkRepository::save);

        // when
        List<Link> links = linkRepository.findAll();

        // then
        assertThat(links).isEmpty();

    }
}
