package ru.tinkoff.edu.java.bot.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final ScrapperClient scrapperClient;

    public List<LinkResponse> getAllLinks(long chatId) {
        return scrapperClient.getAllLinks(chatId).links();
    }

    public Optional<LinkResponse> trackLink(String url, long chatId) {
        return scrapperClient.trackLink(url, chatId);
    }

    public Optional<LinkResponse> untrackLink(String url, long chatId) {
        return scrapperClient.untrackLink(url, chatId);
    }
}
