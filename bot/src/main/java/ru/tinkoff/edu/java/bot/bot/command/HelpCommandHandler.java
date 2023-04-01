package ru.tinkoff.edu.java.bot.bot.command;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.Command;
import ru.tinkoff.edu.java.bot.bot.MessageSender;
import ru.tinkoff.edu.java.bot.bot.State;

import java.util.Arrays;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class HelpCommandHandler implements CommandHandler<SendMessage, SendResponse> {
    private final MessageSender messageSender;

    @Override
    public Command command() {
        return Command.HELP;
    }

    @Override
    public State state() {
        return State.NONE;
    }

    @Override
    public SendMessage handle(Update update) {
        return messageSender.send(update, Arrays.stream(Command.values())
                .map(Command::toString).collect(Collectors.joining("\n")));
    }
}
