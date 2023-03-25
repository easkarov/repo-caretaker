package ru.tinkoff.edu.java.bot.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

@RestController
@RequestMapping("/api/updates")
public class UpdateController {

    @PostMapping
    public ResponseEntity<String> createUpdate(@RequestBody LinkUpdate update) {
        return ResponseEntity.ok().body("Обновление (%s) обработано".formatted(update.id()));
    }
}
