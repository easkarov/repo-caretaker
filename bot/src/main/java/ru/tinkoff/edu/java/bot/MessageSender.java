package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.meta.Command;

import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class MessageSender {

    public SendMessage send(Update update, String text) {
        return send(update, text, true);
    }

    public static Keyboard getKeyboard() {
        return new ReplyKeyboardMarkup(Arrays.stream(Command.values()).map(Command::command).toArray(String[]::new));
    }

    public static Keyboard removeKeyboard() {
        return new ReplyKeyboardRemove();
    }

    public SendMessage send(Update update, String text, boolean withKeyboard) {
        var request = new SendMessage(update.message().chat().id(), text);
        return request.replyMarkup((withKeyboard) ? getKeyboard() : removeKeyboard());
    }

}

