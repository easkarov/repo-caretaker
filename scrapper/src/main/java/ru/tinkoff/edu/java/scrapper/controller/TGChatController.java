package ru.tinkoff.edu.java.scrapper.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class TGChatController {

    @PostMapping(path = "/tg-chat/{id}")
    public ResponseEntity<String> registerChat(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body("Чат зарегистрирован");
    }

    @DeleteMapping(path = "/tg-chat/{id}")
    public ResponseEntity<Object> deleteChat(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().build();

    }

}
