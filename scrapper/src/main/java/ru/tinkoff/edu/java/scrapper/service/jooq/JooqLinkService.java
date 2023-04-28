package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkService;


@Slf4j
public class JooqLinkService extends AbstractLinkService {
    public JooqLinkService(JooqLinkRepository linkRepository, JooqChatRepository chatRepository) {
        super(linkRepository, chatRepository);
    }
}