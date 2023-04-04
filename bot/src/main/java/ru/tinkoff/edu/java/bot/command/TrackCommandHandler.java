package ru.tinkoff.edu.java.bot.command;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.enums.Command;
import ru.tinkoff.edu.java.bot.MessageSender;
import ru.tinkoff.edu.java.bot.enums.State;


@Component
@RequiredArgsConstructor
public class TrackCommandHandler implements CommandHandler<SendMessage, SendResponse> {
    private final MessageSender messageSender;

    @Override
    public Command command() {
        return Command.TRACK;
    }

    @Override
    public State state() {
        return State.TRACK;
    }

    @Override
    public SendMessage handle(Update update) {
        return messageSender.send(update, "Type URL:");
    }
}
