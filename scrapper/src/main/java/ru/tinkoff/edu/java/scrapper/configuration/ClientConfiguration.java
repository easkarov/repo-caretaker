package ru.tinkoff.edu.java.scrapper.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final GitHubConfiguration gitHubConfiguration;
    private final StackOverflowConfiguration stackOverflowConfiguration;

    @Bean
    public GitHubClient gitHubClient() {
        return GitHubClient.fromConfig(gitHubConfiguration);

    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return StackOverflowClient.fromConfig(stackOverflowConfiguration);
    }

    @Bean
    public long linkUpdateSchedulerIntervalMs(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }
}
