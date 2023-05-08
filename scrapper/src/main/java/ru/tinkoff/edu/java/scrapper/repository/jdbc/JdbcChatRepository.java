package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.enums.ChatQuery;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;





@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(ChatQuery.SELECT_ALL.query(), new BeanPropertyRowMapper<>(Chat.class));
    }

    @Override
    public Optional<Chat> findById(long id) {
        return jdbcTemplate.queryForStream(ChatQuery.SELECT_BY_ID.query(),
                new BeanPropertyRowMapper<>(Chat.class), id).findFirst();
    }

    @Override
    public List<Chat> findAllByLink(Link link) {
        return jdbcTemplate.query(ChatQuery.SELECT_BY_LINK.query(),
                new BeanPropertyRowMapper<>(Chat.class), link.getId());
    }

    @Override
    public Chat save(Chat chat) {
        jdbcTemplate.update(ChatQuery.INSERT.query(), chat.getId());
        return chat;
    }

    @Override
    public boolean removeById(long id) {
        return jdbcTemplate.update(ChatQuery.REMOVE_BY_ID.query(), id) == 1;
    }

}
