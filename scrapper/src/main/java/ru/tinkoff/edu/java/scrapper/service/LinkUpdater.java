package ru.tinkoff.edu.java.scrapper.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class LinkUpdater {

    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;
    private final BotClient botClient;

    @Transactional
    public void update() {
        // dummy stub
        for (Link link : linkRepository.findLongUpdated()) {
            var linkChats = chatRepository.findAllByLink(link.getId());

            var update = LinkUpdate
                    .builder()
                    .id(link.getId())
                    .url(URI.create(link.getUrl()))
                    .description("Has been updated")
                    .tgChatIds(linkChats.stream().map(Chat::getId).toList())
                    .build();

            botClient.createUpdate(update);
        }
    }

}
