package ru.tinkoff.edu.java.bot.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import ru.tinkoff.edu.java.bot.bot.meta.Command;
import ru.tinkoff.edu.java.bot.bot.meta.State;

public interface CommandHandler<T extends BaseRequest<T, R>, R extends BaseResponse> {
    Command command();
    State state();
    BaseRequest<T, R> handle(Update update);
    default boolean canHandle(Update update) {
        return update.message().text().startsWith(command().command());
    };
}
