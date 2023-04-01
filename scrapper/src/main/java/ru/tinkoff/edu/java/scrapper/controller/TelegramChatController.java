package ru.tinkoff.edu.java.scrapper.controller;


import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tg-chat")
public class TelegramChatController {

    @PostMapping(path = "/{id}")
    public String registerChat(@PathVariable(name = "id") long id) {
        return "Чат зарегистрирован"; // stub
    }

    @DeleteMapping(path = "/{id}")
    public String deleteChat(@PathVariable(name = "id") long id) {

        return "Чат (%s) успешно удалён".formatted(id);  // stub

    }

}
