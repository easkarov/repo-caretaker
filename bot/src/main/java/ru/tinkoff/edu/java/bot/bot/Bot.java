package ru.tinkoff.edu.java.bot.bot;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;


public class Bot {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("5820628616:AAGCCT7zcGhDBiAh1MlN7XQ9T0_SDg6ZD88");
        bot.setUpdatesListener(updates -> {
            Message msg = updates.get(0).message();
            bot.execute(new SendMessage(msg.from().id(), msg.text()));
            return msg.messageId();
        });
    }
}
