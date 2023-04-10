package ru.tinkoff.edu.java.scrapper.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Subscription;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements SubscriptionRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Subscription> findAll() {
        return jdbcTemplate.query("SELECT * FROM chat_link", this::mapToSubscription);
    }

    @Override
    public int remove(long chatId, long linkId) {
        return jdbcTemplate.update("DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?", chatId, linkId);
    }

    @Override
    public int add(Subscription subscription) {
        return jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)",
                subscription.getChatId(), subscription.getLinkId());
    }

    private Subscription mapToSubscription(ResultSet rs, int rowNum) throws SQLException {
        return new Subscription()
                .setChatId(rs.getLong("chat_id"))
                .setLinkId(rs.getLong("link_id"));
    }
}
