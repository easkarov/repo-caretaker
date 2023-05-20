package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.command.CommandHandler;
import ru.tinkoff.edu.java.bot.enums.State;
import ru.tinkoff.edu.java.bot.dto.HandledUpdate;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CommandsHandler {
    private final Pattern COMMAND_REGEX = Pattern.compile("/(.+)");

    private final List<CommandHandler<?, ?>> commandHandlers;

    public Optional<CommandHandler<?, ?>> findCommandHandler(Update update) {
        return commandHandlers
                .stream()
                .filter(commandHandler -> commandHandler.canHandle(update))
                .findFirst();
    }

    public HandledUpdate handle(Update update) {

        Matcher matcher = COMMAND_REGEX.matcher(update.message().text());

        if (!matcher.matches()) {
            return HandledUpdate.EMPTY;
        }
        var foundCommandHandler = findCommandHandler(update);
        return HandledUpdate.builder()
                .request(foundCommandHandler.map(commandHandler -> commandHandler.handle(update)))
                .newState(foundCommandHandler.map(CommandHandler::state).orElse(State.NONE))
                .build();
    }
}
