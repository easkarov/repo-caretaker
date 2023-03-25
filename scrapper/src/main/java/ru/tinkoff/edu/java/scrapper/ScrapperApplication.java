package ru.tinkoff.edu.java.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfiguration;
import ru.tinkoff.edu.java.scrapper.configuration.StackOverflowConfiguration;


@SpringBootApplication
@EnableConfigurationProperties({
        ApplicationConfig.class,
        GitHubConfiguration.class,
        StackOverflowConfiguration.class}
)
public class ScrapperApplication {
    public static void main(String[] args) {
        var ctx = SpringApplication.run(ScrapperApplication.class, args);
        ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
        StackOverflowClient gh = ctx.getBean(StackOverflowClient.class);
        System.out.println(gh.fetchQuestion(8748378));
        System.out.println(config);
    }
}