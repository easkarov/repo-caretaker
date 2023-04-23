package ru.tinkoff.edu.java.scrapper.service.jdbc;

import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractChatService;


public class JdbcChatService extends AbstractChatService {
    public JdbcChatService(JdbcChatRepository chatRepository) {
        super(chatRepository);
    }
}
