package ru.tinkoff.edu.java.scrapper.jpa;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.enums.LinkQuery;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import util.JpaIntegrationEnvironment;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@Rollback
public class JpaLinkRepositoryTest extends JpaIntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JpaLinkRepository linkRepository;

    @ParameterizedTest
    @ValueSource(strings = {"2023-01-01T00:00:00Z", "2022-01-01T00:00:00Z"})
    @Sql("/sql/fill_in_links.sql")
    public void findLeastRecentlyUpdated__dbHasSomeOldLinks_correctLinkListSize(String dateString) {
        // given
        var dateTime = OffsetDateTime.parse(dateString);

        // when
        List<Link> links = linkRepository.findAllByUpdatedAtBefore(dateTime);

        // then
        List<Link> realLinks = jdbcTemplate.query(LinkQuery.SELECT_LEAST_RECENTLY_UPDATED.query(),
                new BeanPropertyRowMapper<>(Link.class), dateTime);

        assertThat(links).hasSameSizeAs(realLinks);
    }

}
