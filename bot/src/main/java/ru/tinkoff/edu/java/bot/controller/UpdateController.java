package ru.tinkoff.edu.java.bot.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;
import ru.tinkoff.edu.java.bot.service.NotificationService;

@RestController
@RequestMapping("/api/update")
@RequiredArgsConstructor
public class UpdateController {

    private final NotificationService notificationService;

    @PostMapping
    public void createUpdate(@RequestBody LinkUpdate update) {
        notificationService.notify(update);
    }
}
