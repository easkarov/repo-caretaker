package ru.tinkoff.edu.java.scrapper.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TelegramChatController {

    @PostMapping(path = "/tg-chat/{id}")
    public ResponseEntity<String> registerChat(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body("Чат зарегистрирован");
    }

    @DeleteMapping(path = "/tg-chat/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable(name = "id") long id) {

        // TODO: Handle "chat not found" exception

        return ResponseEntity.ok().body("Чат (%s) успешно удалён".formatted(id));

    }

}
