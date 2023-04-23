package ru.tinkoff.edu.java.scrapper.configuration;


import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqAccessConfiguration {

    private final DSLContext dsl;

    @Bean
    public JooqLinkRepository linkRepository() {
        return new JooqLinkRepository(dsl);
    }

    @Bean
    public JooqChatRepository chatRepository() {
        return new JooqChatRepository(dsl);
    }

    @Bean
    public JooqLinkService linkService() {
        return new JooqLinkService(linkRepository(), chatRepository());
    }

    @Bean
    public JooqChatService chatService() {
        return new JooqChatService(chatRepository());
    }
}
