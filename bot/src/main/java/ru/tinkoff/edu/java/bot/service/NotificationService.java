package ru.tinkoff.edu.java.bot.service;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final TelegramBot bot;

    public void notify(LinkUpdate update) {
        for (var id : update.tgChatIds()) {
            var request = new SendMessage(id, "Link %s updated.\n\n%s".formatted(update.url(), update.description()));
            bot.execute(request);
        }
    }
}
