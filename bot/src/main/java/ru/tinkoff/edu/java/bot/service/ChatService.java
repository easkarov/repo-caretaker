package ru.tinkoff.edu.java.bot.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ScrapperClient scrapperClient;

    public boolean registerChat(long chatId) {
        return scrapperClient.registerChat(chatId);
    }

    public boolean unregisterChat(long chatId) {
        return scrapperClient.unregisterChat(chatId);
    }
}
