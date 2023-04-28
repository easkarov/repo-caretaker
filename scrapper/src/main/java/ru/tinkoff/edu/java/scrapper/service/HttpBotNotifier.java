package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

import java.util.List;


@Slf4j
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
@Service
@RequiredArgsConstructor
public class HttpBotNotifier implements BotNotifier {

    private final BotClient botClient;

    @Override
    public void notify(List<LinkUpdate> updates) {
        log.info("Sending message through HTTP");
        for (var update : updates) {
            botClient.sendUpdate(update);
        }
    }
}
