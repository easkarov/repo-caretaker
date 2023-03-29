package ru.tinkoff.edu.java.bot.bot.command;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.Sender;
import ru.tinkoff.edu.java.bot.bot.State;


@Component
@RequiredArgsConstructor
public class LinkCommand implements Command<SendMessage, SendResponse> {
    private final Sender sender;

    @Override
    public String name() {
        return "link";
    }

    @Override
    public String description() {
        return "Track link";
    }

    @Override
    public State state() {
        return State.TRACK_LINK;
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> handle(Update update) {
        return sender.send(update, "You chose /link command");
    }
}
