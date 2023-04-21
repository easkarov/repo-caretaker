package ru.tinkoff.edu.java.scrapper.configuration.database;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.ChatServiceImpl;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkServiceImpl;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@RequiredArgsConstructor
public class JpaAccessConfiguration {

    @PersistenceContext
    private final EntityManager entityManager;

    @Bean
    public LinkRepository linkRepository() {
        return new JpaLinkRepository(entityManager);
    }

    @Bean
    public ChatRepository chatRepository() {
        return new JpaChatRepository(entityManager);
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
