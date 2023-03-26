package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinkResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/links")
public class LinkController {
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader(name = TG_CHAT_HEADER) long tgChatId) {
        return new ListLinkResponse(List.of(), 0);
    }

    @PostMapping
    public LinkResponse addLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        // TODO: Handle "chat not found" exception

        return new LinkResponse(UUID.randomUUID().hashCode(), addLinkRequest.link());
    }

    @DeleteMapping
    public LinkResponse removeLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {

        // TODO: Handle "chat not found" and "link not found" exceptions

        return new LinkResponse(UUID.randomUUID().hashCode(), removeLinkRequest.link());
    }
}
