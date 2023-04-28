package ru.tinkoff.edu.java.scrapper.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JdbcLinkRepository linkRepository() {
        return new JdbcLinkRepository(jdbcTemplate);
    }

    @Bean
    public JdbcChatRepository chatRepository() {
        return new JdbcChatRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLinkService linkService() {
        return new JdbcLinkService(linkRepository(), chatRepository());
    }

    @Bean
    public JdbcChatService chatService() {
        return new JdbcChatService(chatRepository());
    }
}
