package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.HandledUpdate;
import ru.tinkoff.edu.java.bot.enums.State;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final MessageSender messageSender;
    private final LinkService linkService;

    // probably needs refactoring
    public HandledUpdate handle(Update update, State state) {

        String message;
        State newState = State.NONE;

        switch (state) {
            case TRACK -> {
                var response = linkService.trackLink(update.message().text(), update.message().chat().id());
                message = (response.isPresent()) ? "Got this link tracked" : "Unable to track, sorry";
            }
            case UNTRACK -> {
                var response = linkService.untrackLink(update.message().text(), update.message().chat().id());
                message = (response.isPresent()) ? "Got this link untracked" : "Unable to untrack, sorry";
            }
            default -> message = "Unknown command. Try using /help.";
        }

        return HandledUpdate.builder()
                .request(Optional.of(messageSender.send(update, message)))
                .newState(newState)
                .build();
    }
}
