package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.enums.LinkQuery;
import ru.tinkoff.edu.java.scrapper.exception.DBException;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Link> findAll() {
        return jdbcTemplate.query(LinkQuery.SELECT_ALL.query(), this::mapRowToLink);
    }


    @Override
    public List<Link> findAllByChat(long chatId) {
        return jdbcTemplate.query(LinkQuery.SELECT_BY_CHAT.query(), this::mapRowToLink, chatId);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        Stream<Link> links = jdbcTemplate.queryForStream(LinkQuery.SELECT_BY_URL.query(), this::mapRowToLink, url);
        return links.findFirst();
    }

    @Override
    public Optional<Link> findById(long id) {
        Stream<Link> links = jdbcTemplate.queryForStream(LinkQuery.SELECT_BY_ID.query(), this::mapRowToLink, id);
        return links.findFirst();
    }

    @Override
    public List<Link> findLeastRecentlyUpdated(OffsetDateTime olderThan) {
        return jdbcTemplate.query(LinkQuery.SELECT_LEAST_RECENTLY_UPDATED.query(),
                this::mapRowToLink, olderThan);
    }

    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            jdbcTemplate.update(LinkQuery.INSERT.query(), link.getUrl());
            return findByUrl(link.getUrl()).orElseThrow(() -> new DBException("Failed to save link")); // TODO: handle exception
        }

        jdbcTemplate.update(LinkQuery.UPDATE.query(), link.getUrl(), link.getUpdatedAt(), link.getId());
        return findById(link.getId()).orElseThrow(() -> new DBException("Failed to update link")); // TODO: handle exception
    }

    @Override
    public boolean removeById(long id) {
        return jdbcTemplate.update(LinkQuery.REMOVE_BY_ID.query(), id) >= 1;
    }

    @Override
    public boolean addToChat(long chatId, long linkId) {
        var ifExists = jdbcTemplate.queryForObject(LinkQuery.EXISTS_IN_CHAT.query(), boolean.class, chatId, linkId);
        if (ifExists == null || ifExists) return false;
        jdbcTemplate.update(LinkQuery.ADD_TO_CHAT.query(), chatId, linkId);
        return true;
    }

    @Override
    public boolean removeFromChat(long chatId, long linkId) {
        return jdbcTemplate.update(LinkQuery.REMOVE_FROM_CHAT.query(), chatId, linkId) >= 1;
    }

    private Link mapRowToLink(ResultSet row, int rowNum) throws SQLException {
        return new Link()
                .setId(row.getLong("id"))
                .setUrl(row.getString("url"))
                .setUpdatedAt(row.getObject("updated_at", OffsetDateTime.class));
    }
}
