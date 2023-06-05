package ru.tinkoff.edu.java.scrapper.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;


@RequiredArgsConstructor
public class BotClient {
    private static final String BASE_URL = "http://bot:8000/api/update";

    private final WebClient webClient;

    public static BotClient create() {
        return create(BASE_URL);
    }

    public static BotClient create(String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
        return new BotClient(webClient);
    }

    public boolean sendUpdate(LinkUpdate linkUpdate) {
        return Boolean.TRUE.equals(webClient
                .post()
                .uri(BASE_URL)
                .body(BodyInserters.fromValue(linkUpdate))
                .exchangeToMono(clientResponse -> Mono.just(!clientResponse.statusCode().isError()))
                .block());
    }

}
