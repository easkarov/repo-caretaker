package ru.tinkoff.edu.java.scrapper.service.jooq;

import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractChatService;


public class JooqChatService extends AbstractChatService {

    public JooqChatService(JooqChatRepository chatRepository) {
        super(chatRepository);
    }
}
