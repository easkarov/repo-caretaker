package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Chat;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {

    public static final String SELECT_BY_ID = "SELECT * FROM chat WHERE id = ?";
    public static final String INSERT = """
            INSERT INTO chat(id) VALUES(?)
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat", new BeanPropertyRowMapper<>(Chat.class));
    }

    @Override
    public Optional<Chat> findById(long id) {
        return jdbcTemplate.queryForStream(SELECT_BY_ID,
                new BeanPropertyRowMapper<>(Chat.class), id).findFirst();
    }

    @Override
    public Chat save(Chat chat) {
        if (findById(chat.getId()).isEmpty()) {
            jdbcTemplate.update(INSERT, chat.getId());
        }
        return chat;
    }

    @Override
    public boolean removeById(long id) {
        return jdbcTemplate.update("DELETE FROM chat WHERE id = ?", id) >= 1;
    }

}
