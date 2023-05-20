package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.scrapper.configuration.client.GitHubConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.client.StackOverflowConfiguration;


@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
        ApplicationProperties.class,
        GitHubConfiguration.class,
        StackOverflowConfiguration.class}
)
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
