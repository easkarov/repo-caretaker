package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.configuration.GitHubConfiguration;
import ru.tinkoff.edu.java.scrapper.dto.response.GitHubRepositoryResponse;

import java.util.Optional;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Slf4j
public class GitHubClient {
    private static final String REPO_ENDPOINT = "/repos/%s/%s";
    private static final String COMMIT_ENDPOINT = "/repos/%s/%s/commits?per_page=%s&page=%s";

    private final WebClient webClient;

    public static GitHubClient fromConfig(GitHubConfiguration config) {
        WebClient webClient = WebClient.builder()
                .baseUrl(config.baseUrl())
                .defaultHeader("X-GitHub-Api-Version", config.apiVersion())
                .defaultHeader("Authorization", "Bearer %s".formatted(config.apiKey()))
                .build();
        return new GitHubClient(webClient);

    }

    public Optional<GitHubRepositoryResponse> fetchRepository(String owner, String repo) {
        String uri = String.format(REPO_ENDPOINT, owner, repo);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class)
                .onErrorResume(exception -> Mono.empty())
                .blockOptional();

    }

    public Optional<Integer> fetchCommitsNumber(String owner, String repo) {
        Pattern pattern = Pattern.compile("next.*page=(?<number>\\d+).*last");
        String uri = String.format(COMMIT_ENDPOINT, owner, repo, 1, 1);

        // get last page number from header (GitHub api specific)
        var header = webClient.get()
                .uri(uri)
                .exchangeToMono(response -> Mono.just(response.headers().header("link").get(0)))
                .onErrorResume(exception -> Mono.empty())
                .blockOptional();

        return header.map(h -> {
            var matcher = pattern.matcher(header.get());
            matcher.find();
            return Integer.parseInt(matcher.group("number"));
        });
    }
}
