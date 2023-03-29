package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.bot.command.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class UpdatesProcessor implements UpdatesListener {

    private TelegramBot bot;
    private final CommandManager commandManager;
    private final MessageHandler messageHandler;
    private final Sender sender;
    private Map<Long, State> userStates = new HashMap<>();

    public void setBot(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {

            if (update.message() == null) {
                continue;
            }

            Long userId = update.message().from().id();
            State state = userStates.get(userId);

            HandledUpdate handledUpdate = commandManager.handle(update);
            if (handledUpdate.request().isPresent()) {
                bot.execute(handledUpdate.request().get());
                userStates.put(userId, handledUpdate.newState());
                continue;
            }

            handledUpdate = messageHandler.handle(update, state);
            if (handledUpdate.request().isPresent()) {
                bot.execute(handledUpdate.request().get());
                userStates.put(userId, handledUpdate.newState());
            }
        }

        return CONFIRMED_UPDATES_ALL;
    }
}