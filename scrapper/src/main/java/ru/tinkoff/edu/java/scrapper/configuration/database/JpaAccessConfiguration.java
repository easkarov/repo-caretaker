package ru.tinkoff.edu.java.scrapper.configuration.database;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
@RequiredArgsConstructor
public class JpaAccessConfiguration {

    @PersistenceContext
    private final EntityManager entityManager;

    @Bean
    public JpaLinkRepository linkRepository() {
        return new JpaLinkRepository(entityManager);
    }

    @Bean
    public JpaChatRepository chatRepository() {
        return new JpaChatRepository(entityManager);
    }
}
