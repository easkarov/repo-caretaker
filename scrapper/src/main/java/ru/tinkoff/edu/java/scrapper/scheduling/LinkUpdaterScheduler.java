package ru.tinkoff.edu.java.scrapper.scheduling;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

import java.net.URI;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@linkUpdateSchedulerIntervalMs}")
    public void update() {
        var link = LinkUpdate.builder()
                .id(1L)
                .url(URI.create("www"))
                .description("Обновление получено").tgChatIds(List.of(1L, 2L, 3L))
                .build();
        log.info(Boolean.toString(botClient.createUpdate(link)));
    }
}
