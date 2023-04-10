package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", this::mapRowToLink);
    }

    @Override
    public int add(Link link) {
        if (link.getId() == null) {
            return jdbcTemplate.update("INSERT INTO link(url) VALUES(?)", link.getUrl());
        }
        return jdbcTemplate.update("INSERT INTO link(id, url) VALUES(?, ?)", link.getId(), link.getUrl());
    }

    @Override
    public int bindToChat(Link link, long chatId) {
        return jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", link.getId(), chatId);
    }

    @Override
    public int remove(long id) {
        return jdbcTemplate.update("DELETE FROM link WHERE id = ?", id);
    }
    private Link mapRowToLink(ResultSet row, int rowNum) throws SQLException {
        return new Link()
                .setId(row.getLong("id"))
                .setUrl(row.getString("url"))
                .setUpdatedAt(row.getTimestamp("updated_at").toInstant());
    }
}
