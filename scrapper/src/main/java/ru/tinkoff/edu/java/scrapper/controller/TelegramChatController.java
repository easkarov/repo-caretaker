package ru.tinkoff.edu.java.scrapper.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.ChatService;


@RestController
@RequestMapping("/api/tg-chat")
@RequiredArgsConstructor
public class TelegramChatController {

    private final ChatService chatService;

    @PostMapping(path = "/{id}")
    public void registerChat(@PathVariable(name = "id") long id) {
        chatService.register(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteChat(@PathVariable(name = "id") long id) {
        chatService.unregister(id);
    }

}
