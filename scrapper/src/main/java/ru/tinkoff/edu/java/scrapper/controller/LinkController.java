package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ListLinkResponse> getLinks(@RequestHeader(name = TG_CHAT_HEADER) long tgChatId) {
        var response = new ListLinkResponse(List.of(), 0);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        // TODO: Handle "chat not found" exception

        var response = new LinkResponse(UUID.randomUUID().timestamp(), addLinkRequest.link());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {

        // TODO: Handle "chat not found" and "link not found" exceptions

        var response = new LinkResponse(UUID.randomUUID().timestamp(), removeLinkRequest.link());
        return ResponseEntity.ok().body(response);
    }
}
