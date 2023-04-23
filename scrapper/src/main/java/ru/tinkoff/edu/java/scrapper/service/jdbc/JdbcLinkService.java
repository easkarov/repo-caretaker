package ru.tinkoff.edu.java.scrapper.service.jdbc;

import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkService;


public class JdbcLinkService extends AbstractLinkService {
    public JdbcLinkService(JdbcLinkRepository linkRepository, JdbcChatRepository chatRepository) {
        super(linkRepository, chatRepository);
    }
}
