package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


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
        List<Link> links = jdbcTemplate.query(JdbcLinkQueries.SELECT_BY_ID.query(), this::mapRowToLink, url);
        return (links.isEmpty() ? Optional.empty() : Optional.of(links.get(0)));
    }

    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            return jdbcTemplate.queryForObject(JdbcLinkQueries.INSERT.query(), this::mapRowToLink,
                    link.getUrl(), link.getUrl());
        }

        return jdbcTemplate.queryForObject(JdbcLinkQueries.UPDATE.query(), this::mapRowToLink,
                link.getUrl(), link.getUpdatedAt(), link.getId(), link.getId());
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
                .setUpdatedAt(row.getTimestamp("updated_at").toInstant());
    }
}
