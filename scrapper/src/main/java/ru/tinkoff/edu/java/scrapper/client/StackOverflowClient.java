package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.StackOverflowQuestionsResponse;

import java.util.Optional;

@Component
public class StackOverflowClient {
    private static final String GET_QUESTIONS_ENDPOINT = "/questions/%s?site=stackoverflow";

    private final WebClient webClient;

    public StackOverflowClient(@Qualifier("stackoverflowBaseClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Optional<StackOverflowQuestionResponse> fetchQuestion(long questionId) {
        String uri = String.format(GET_QUESTIONS_ENDPOINT, questionId);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(StackOverflowQuestionsResponse.class)
                .blockOptional()
                .filter(questions -> !questions.items().isEmpty())
                .map(questions -> questions.items().get(0));
    }
}
