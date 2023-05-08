package ru.tinkoff.edu.java.bot.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;
import ru.tinkoff.edu.java.bot.service.NotificationService;

@Slf4j
@RestController
@RequestMapping("/api/update")
@RequiredArgsConstructor
public class UpdateController {

    private final NotificationService notificationService;

    @PostMapping
    public void sendUpdate(@RequestBody LinkUpdate update) {
        log.info("Got update from HTTP: {}", update);
        notificationService.notify(update);
    }
}
