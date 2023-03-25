package ru.tinkoff.edu.java.scrapper.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final GitHubConfiguration gitHubConfiguration;
    private final StackOverflowConfiguration stackOverflowConfiguration;

    @Bean("githubBaseClient")
    public WebClient githubBaseClient() {
        return WebClient.builder()
                .baseUrl(gitHubConfiguration.baseUrl())
                .defaultHeader("X-GitHub-Api-Version", gitHubConfiguration.apiVersion())
                .build();
    }


    @Bean("stackoverflowBaseClient")
    public WebClient stackoverflowBaseClient() {
        return WebClient.builder()
                .baseUrl(stackOverflowConfiguration.baseUrl())
                .build();
    }
}
