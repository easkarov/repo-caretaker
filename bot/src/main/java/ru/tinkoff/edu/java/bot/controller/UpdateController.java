package ru.tinkoff.edu.java.bot.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

@RestController
@RequestMapping("/bot/api")
public class UpdateController {

    @PostMapping( path = "/updates")
    public ResponseEntity<String> createUpdate(@RequestBody(required = false) LinkUpdate update) {
        System.out.println(update);
        return ResponseEntity.ok().body("Обновление обработано");
    }
}
