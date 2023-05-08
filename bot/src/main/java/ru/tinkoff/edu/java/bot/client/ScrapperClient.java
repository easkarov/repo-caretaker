package ru.tinkoff.edu.java.bot.client;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.response.ListLinkResponse;


@RequiredArgsConstructor
public class ScrapperClient {
    private static final String TG_CHAT_ENDPOINT = "/tg-chat/%s";
    private static final String LINK_ENDPOINT = "/link";
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;

    public ScrapperClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public Optional<LinkResponse> trackLink(String url, long chatId) {
        return webClient
                .post()
                .uri(LINK_ENDPOINT)
                .header(TG_CHAT_HEADER, String.valueOf(chatId))
                .body(BodyInserters.fromValue(new AddLinkRequest(url)))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .onErrorResume(exception -> Mono.empty())
                .blockOptional();
    }

    public Optional<LinkResponse> untrackLink(String url, long chatId) {
        return webClient
                .method(HttpMethod.DELETE)
                .uri(LINK_ENDPOINT)
                .header(TG_CHAT_HEADER, String.valueOf(chatId))
                .body(BodyInserters.fromValue(new RemoveLinkRequest(url)))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .onErrorResume(exception -> Mono.empty())
                .blockOptional();
    }

    public ListLinkResponse getAllLinks(long chatId) {
        return webClient
                .get()
                .uri(LINK_ENDPOINT)
                .header(TG_CHAT_HEADER, String.valueOf(chatId))
                .retrieve()
                .bodyToMono(ListLinkResponse.class)
                .onErrorResume(exception -> Mono.empty())
                .block();
    }

    public boolean registerChat(long chatId) {
        return Boolean.TRUE.equals(webClient
                .post()
                .uri(TG_CHAT_ENDPOINT.formatted(chatId))
                .exchangeToMono(clientResponse -> Mono.just(!clientResponse.statusCode().isError()))
                .block());
    }

    public boolean unregisterChat(long chatId) {
        return Boolean.TRUE.equals(webClient
                .delete()
                .uri(TG_CHAT_ENDPOINT.formatted(chatId))
                .exchangeToMono(clientResponse -> Mono.just(!clientResponse.statusCode().isError()))
                .block());
    }

}
