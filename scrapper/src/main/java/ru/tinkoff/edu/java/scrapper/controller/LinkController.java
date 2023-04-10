package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
public class LinkController {
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    private final LinkService linkService;

    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader(name = TG_CHAT_HEADER) long tgChatId) {
        // TODO: use MapStruct
        List<LinkResponse> links = linkService
                .listAll(tgChatId)
                .stream()
                .map(link -> new LinkResponse(link.getId(), URI.create(link.getUrl())))
                .toList();

        return new ListLinkResponse(links, links.size());
    }

    @Transactional
    @PostMapping
    public LinkResponse trackLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody AddLinkRequest addLinkRequest
    ) {
        Link link = linkService.track(tgChatId, addLinkRequest.link());

        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }

    @DeleteMapping
    public LinkResponse untrackLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        Link link = linkService.untrack(tgChatId, removeLinkRequest.link());

        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }
}
