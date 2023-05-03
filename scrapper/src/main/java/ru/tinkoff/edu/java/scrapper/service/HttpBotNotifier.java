package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;


@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class HttpBotNotifier implements BotNotifier {

    private final BotClient botClient;

    @Override
    public void notify(LinkUpdate update) {
        log.info("Sending message through HTTP");
        botClient.sendUpdate(update);
    }
}
