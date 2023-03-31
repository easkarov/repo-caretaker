package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.Command;
import ru.tinkoff.edu.java.bot.bot.command.HelpCommand;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final Pattern COMMAND_REGEX = Pattern.compile("/(.+)");

    public final List<Command<?, ?>> commands;
    public final HelpCommand helpCommand;

    public List<Command<?, ?>> getCommands() {
        return commands;
    }

    public Command<?, ?> findCommand(Update update) {
        return commands
                .stream()
                .filter(command -> command.canHandle(update))
                .findFirst()
                .orElse(helpCommand);
    }

    public HandledUpdate handle(Update update) {

        Matcher matcher = COMMAND_REGEX.matcher(update.message().text());

        if (!matcher.matches()) {
            return HandledUpdate.builder()
                    .request(Optional.empty())
                    .newState(State.NONE)
                    .build();
        }

        var foundCommand = findCommand(update);
        return HandledUpdate.builder()
                .request(Optional.of(foundCommand.handle(update)))
                .newState(foundCommand.state())
                .build();
    }
}