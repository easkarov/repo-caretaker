package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
public class LinkController {
    private static final String TG_CHAT_HEADER = "Tg-Chat-Id";

    private final LinkRepository linkRepository;

    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader(name = TG_CHAT_HEADER) long tgChatId) {
//        return new ListLinkResponse(List.of(), 0);
        // TODO: use MapStruct
        List<LinkResponse> links = linkRepository
                .findAll()
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
        return new LinkResponse(UUID.randomUUID().hashCode(), URI.create(addLinkRequest.link()));
    }

    @DeleteMapping
    public LinkResponse untrackLink(
            @RequestHeader(name = TG_CHAT_HEADER) long tgChatId,
            @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        return new LinkResponse(UUID.randomUUID().hashCode(), URI.create(removeLinkRequest.link()));
    }
}
