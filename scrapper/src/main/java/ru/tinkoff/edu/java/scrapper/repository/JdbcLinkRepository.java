package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.enums.JdbcLinkQueries;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Link> findAll() {
        return jdbcTemplate.query(JdbcLinkQueries.SELECT_ALL.query(), this::mapRowToLink);
    }


    @Override
    public List<Link> findAllByChat(long chatId) {
        return jdbcTemplate.query(JdbcLinkQueries.SELECT_BY_CHAT.query(), this::mapRowToLink, chatId);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        Stream<Link> links = jdbcTemplate.queryForStream(JdbcLinkQueries.SELECT_BY_URL.query(), this::mapRowToLink, url);
        return links.findFirst();
    }

    @Override
    public Optional<Link> findById(long id) {
        Stream<Link> links = jdbcTemplate.queryForStream(JdbcLinkQueries.SELECT_BY_ID.query(), this::mapRowToLink, id);
        return links.findFirst();
    }

    @Override
    public List<Link> findLongUpdated(TemporalAmount delta) {
        return jdbcTemplate.query(JdbcLinkQueries.SELECT_LONG_UPDATED.query(),
                this::mapRowToLink, OffsetDateTime.now().minus(delta));
    }

    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            jdbcTemplate.update(JdbcLinkQueries.INSERT.query(), link.getUrl());
            return findByUrl(link.getUrl()).orElseThrow();
        }

        jdbcTemplate.update(JdbcLinkQueries.UPDATE.query(),
                link.getUrl(), link.getUpdateData(), link.getUpdatedAt(), link.getId());
        return findById(link.getId()).orElseThrow();
    }

    @Override
    public boolean addToChat(long chatId, long linkId) {
        if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(JdbcLinkQueries.EXISTS_IN_CHAT.query(), boolean.class,
                chatId, linkId))) {
            return false;
        }
        jdbcTemplate.update(JdbcLinkQueries.ADD_TO_CHAT.query(), chatId, linkId);
        return true;
    }

    @Override
    public boolean removeById(long id) {
        return jdbcTemplate.update(JdbcLinkQueries.REMOVE_BY_ID.query(), id) >= 1;
    }

    @Override
    public boolean removeFromChat(long chatId, long linkId) {
        return jdbcTemplate.update(JdbcLinkQueries.REMOVE_FROM_CHAT.query(), chatId, linkId) >= 1;
    }

    private Link mapRowToLink(ResultSet row, int rowNum) throws SQLException {
        return new Link()
                .setId(row.getLong("id"))
                .setUrl(row.getString("url"))
                .setUpdatedAt(row.getObject("updated_at", OffsetDateTime.class))
                .setUpdateData(row.getString("update_data"));
    }
}
