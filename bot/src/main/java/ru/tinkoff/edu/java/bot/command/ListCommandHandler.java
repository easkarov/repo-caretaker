package ru.tinkoff.edu.java.bot.command;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.enums.Command;
import ru.tinkoff.edu.java.bot.MessageSender;
import ru.tinkoff.edu.java.bot.enums.State;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ListCommandHandler implements CommandHandler<SendMessage, SendResponse> {
    private final MessageSender messageSender;
    private final LinkService linkService;

    @Override
    public Command command() {
        return Command.LIST;
    }

    @Override
    public State state() {
        return State.NONE;
    }

    @Override
    public SendMessage handle(Update update) {
        List<LinkResponse> linkResponses = linkService.getAllLinks(update.message().chat().id());

        if (linkResponses.isEmpty()) return messageSender.send(update, "There are no tracked links");

        var linksMsg = linkResponses
                .stream()
                .map(LinkResponse::url)
                .map(URI::toString)
                .collect(Collectors.joining("\n"));

        return messageSender.send(update, "Your links:\n%s".formatted(linksMsg));
    }
}
