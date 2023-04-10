package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Chat;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(Chat.class));
    }

    @Override
    public Chat add(Chat chat) {
        var insertQuery = "INSERT INTO chat (id) VALUES (?)";
        var selectQuery = "SELECT * FROM chat WHERE id = ?";

        jdbcTemplate.update(insertQuery, chat.getId());

        return jdbcTemplate.queryForObject(selectQuery, new BeanPropertyRowMapper<>(Chat.class), chat.getId());

    }

    @Override
    public boolean remove(long id) {
        return jdbcTemplate.update("DELETE FROM chat WHERE id = ?", id) >= 1;
    }

}
