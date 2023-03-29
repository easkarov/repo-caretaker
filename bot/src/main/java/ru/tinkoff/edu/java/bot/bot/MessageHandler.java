package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.HelpCommand;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    public final CommandManager commandManager;
    public final HelpCommand helpCommand;
    public final Sender sender;

    // NEEDS REFACTORING
    public HandledUpdate handle(Update update, State state) {
        BaseRequest<?, ?> request;
        State newState = State.NONE;
        if (state == State.TRACK_LINK) {
            request = sender.send(update, "Okay, I will process your Link, thanks!");
        } else {
            request = sender.send(update, "Use /help lol");
        }

        return HandledUpdate.builder()
                .request(Optional.of(request))
                .newState(newState)
                .build();
    }
}
