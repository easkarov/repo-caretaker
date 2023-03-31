package ru.tinkoff.edu.java.bot.bot.command;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.CommandHandler;
import ru.tinkoff.edu.java.bot.bot.Sender;
import ru.tinkoff.edu.java.bot.bot.State;


@Component
@RequiredArgsConstructor
public class HelpCommand implements Command<SendMessage, SendResponse> {
    private final Sender messageSender;
    private final CommandHandler commandHandler;

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "Help command";
    }

    @Override
    public State state() {
        return State.NONE;
    }

    @Override
    public BaseRequest<SendMessage, SendResponse> handle(Update update) {

        var commandsString = commandHandler
                .getCommands()
                .stream()
                .map(Command::toString);
        return messageSender.send(update, "You chose /help command");
    }
}
