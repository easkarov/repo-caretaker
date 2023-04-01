package ru.tinkoff.edu.java.bot.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final ScrapperClient scrapperClient;

    public List<LinkResponse> getAllLinks(long chatId) {
        return scrapperClient.getLinks(chatId).links();
    }
}
