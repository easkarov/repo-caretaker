package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;

@Component
public class GitHubClient {
    private static final String GET_REPO_ENDPOINT = "/repos/%s/%s";

    private final WebClient webClient;

    public GitHubClient(@Qualifier("githubBaseClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public GitHubRepositoryResponse fetchRepository(String owner, String repo) {
        String uri = String.format(GET_REPO_ENDPOINT, owner, repo);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class)
                .block();
    }
}
