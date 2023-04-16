package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfiguration;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;

import java.util.Optional;


@RequiredArgsConstructor
public class GitHubClient {
    private static final String GET_REPO_ENDPOINT = "/repos/%s/%s";

    private final WebClient webClient;

    public static GitHubClient fromConfig(GitHubConfiguration config) {
        WebClient webClient = WebClient.builder()
                .baseUrl(config.baseUrl())
                .defaultHeader("X-GitHub-Api-Version", config.apiVersion())
                .build();

        return new GitHubClient(webClient);

    }

    public Optional<GitHubRepositoryResponse> fetchRepository(String owner, String repo) {
        String uri = String.format(GET_REPO_ENDPOINT, owner, repo);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class)
                .onErrorResume(WebClientResponseException.class, exception -> Mono.empty())
                .blockOptional();

    }
}
