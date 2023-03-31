package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;


@Component
public class MessageSender implements Sender {
    public SendMessage send(Update update, String text) {
        return new SendMessage(update.message().chat().id(), text);
    }
}
