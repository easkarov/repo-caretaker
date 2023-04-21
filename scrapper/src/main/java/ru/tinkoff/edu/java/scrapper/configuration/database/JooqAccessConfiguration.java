package ru.tinkoff.edu.java.scrapper.configuration.database;


import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.ChatServiceImpl;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkServiceImpl;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqAccessConfiguration {

    private final DSLContext dsl;

    @Bean
    public LinkRepository linkRepository() {
        return new JooqLinkRepository(dsl);
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JooqChatRepository(dsl);
    }

    @Bean
    public LinkService linkService() {
        return new LinkServiceImpl(linkRepository(), chatRepository());
    }

    @Bean
    public ChatService chatService() {
        return new ChatServiceImpl(chatRepository());
    }
}
