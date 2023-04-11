package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
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

    @Override
    public List<Link> findAll(long chatId) {
        return jdbcTemplate.query("""
                          SELECT link.* FROM link
                          JOIN chat_link ON link.id = link_id WHERE chat_id = ?
                        """,
                this::mapRowToLink, chatId);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        var selectQuery = "SELECT * FROM link WHERE url = ?";
        List<Link> links = jdbcTemplate.query(selectQuery, this::mapRowToLink, url);
        return (links.isEmpty() ? Optional.empty() : Optional.of(links.get(0)));
    }

    @Override
    public Link add(Link link) {
        var insertQuery = "INSERT INTO link(url) VALUES(?)";
        var selectQuery = "SELECT * FROM LINK WHERE id = ?";
        var keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var statement = con.prepareStatement(insertQuery, new String[]{"id"});
            statement.setString(1, link.getUrl());
            return statement;
        }, keyHolder);
        return jdbcTemplate.queryForObject(selectQuery, this::mapRowToLink, keyHolder.getKey());
    }

    @Override
    public boolean addToChat(long chatId, long linkId) {
        var existsQuery = "SELECT EXISTS(SELECT * FROM chat_link WHERE (chat_id, link_id) = (?, ?))";
        var insertQuery = "INSERT INTO chat_link VALUES (?, ?)";

        if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(existsQuery, boolean.class, chatId, linkId))) {
            return false;
        }
        jdbcTemplate.update(insertQuery, chatId, linkId);
        return true;
    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update("DELETE FROM link WHERE id = ?", id) >= 1;
    }

    @Override
    public boolean removeFromChat(long chatId, long linkId) {
        return jdbcTemplate.update("DELETE FROM chat_link WHERE (chat_id, link_id) = (?, ?)", chatId, linkId) >= 1;
    }

    private Link mapRowToLink(ResultSet row, int rowNum) throws SQLException {
        return new Link()
                .setId(row.getLong("id"))
                .setUrl(row.getString("url"))
                .setUpdatedAt(row.getTimestamp("updated_at").toInstant());
    }
}
