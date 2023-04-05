package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.enums.State;
import ru.tinkoff.edu.java.bot.dto.HandledUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class UpdatesHandler implements UpdatesListener {

    private final TelegramBot bot;
    private final CommandsHandler commandsHandler;
    private final MessageHandler messageHandler;
    private final Map<Long, State> states = new HashMap<>();

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {

            if (update.message() == null) {
                continue;
            }

            Long chatId = update.message().chat().id();
            State state = states.getOrDefault(chatId, State.NONE);

            HandledUpdate handledUpdate = commandsHandler.handle(update);
            if (handledUpdate.request().isPresent()) {
                bot.execute(handledUpdate.request().get());
                states.put(chatId, handledUpdate.newState());
                continue;
            }

            handledUpdate = messageHandler.handle(update, state);
            if (handledUpdate.request().isPresent()) {
                bot.execute(handledUpdate.request().get());
                states.put(chatId, handledUpdate.newState());
            }
        }

        return CONFIRMED_UPDATES_ALL;
    }
}