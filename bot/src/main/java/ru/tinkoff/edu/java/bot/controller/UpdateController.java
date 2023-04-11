package ru.tinkoff.edu.java.bot.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

@RestController
@RequestMapping("/api/update")
public class UpdateController {

    @PostMapping
    public String createUpdate(@RequestBody LinkUpdate update) {
        return "Обновление (%s) обработано".formatted(update.id());  // stub
    }
}
