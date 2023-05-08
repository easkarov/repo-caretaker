package ru.tinkoff.edu.java.bot.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;


@Slf4j
@Service
@RabbitListener(queues = "${app.rabbitmq.queue}")
@RequiredArgsConstructor
public class ScrapperQueueListener {

    private final NotificationService notificationService;

    @RabbitHandler
    public void receiveUpdate(LinkUpdate update) {
        log.info("Got update from Queue: {}", update);
        notificationService.notify(update);
    }
}
