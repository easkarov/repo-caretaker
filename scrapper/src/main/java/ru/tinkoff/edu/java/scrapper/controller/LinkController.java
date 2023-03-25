package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;

@RestController
@RequestMapping("/api")
public class LinkController {
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    @GetMapping()
    public ResponseEntity<String> getLinks(@RequestHeader(name = TG_CHAT_HEADER) long tgChatId) {
        return ResponseEntity.ok().body("Ссылки успешно получены");
    }

    @PostMapping(path = "/links")
    public ResponseEntity<LinkResponse> addLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        var response = new LinkResponse(tgChatId, addLinkRequest.link());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(path = "/links")
    public ResponseEntity<LinkResponse> deleteLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        var response = new LinkResponse(tgChatId, removeLinkRequest.link());
        return ResponseEntity.ok().body(response);
    }
}
