package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationProperties;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;


@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
@RequiredArgsConstructor
public class QueueBotNotifier implements BotNotifier {

    private final ApplicationProperties properties;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void notify(LinkUpdate update) {
        log.info("Sending message through Queue");
        rabbitTemplate.convertAndSend(
                properties.rabbitmq().exchange(),
                properties.rabbitmq().key(),
                update
        );
    }
}
