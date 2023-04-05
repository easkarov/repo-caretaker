package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.configuration.StackOverflowConfiguration;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionsResponse;

import java.util.Optional;

@RequiredArgsConstructor
public class StackOverflowClient {
    private static final String GET_QUESTIONS_ENDPOINT = "/questionsss/%s?site=stackoverflow";

    private final WebClient webClient;

    public static StackOverflowClient fromConfig(StackOverflowConfiguration config) {
        WebClient webClient = WebClient.builder()
                .baseUrl(config.baseUrl())
                .build();

        return new StackOverflowClient(webClient);
    }

    public Optional<StackOverflowQuestionResponse> fetchQuestion(long questionId) {
        String uri = String.format(GET_QUESTIONS_ENDPOINT, questionId);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(StackOverflowQuestionsResponse.class)
                .onErrorResume(WebClientResponseException.class, exception -> Mono.empty())
                .blockOptional()
                .filter(questions -> !questions.items().isEmpty())
                .map(questions -> questions.items().get(0));
    }
}
