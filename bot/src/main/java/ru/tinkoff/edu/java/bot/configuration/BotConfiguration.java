package ru.tinkoff.edu.java.bot.configuration;


import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.UpdatesProcessor;
import ru.tinkoff.edu.java.bot.command.CommandHandler;

import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationConfig applicationConfig;
    private final UpdatesProcessor updatesProcessor;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(applicationConfig.botToken());
        bot.setUpdatesListener(updatesProcessor);
        updatesProcessor.setBot(bot);
        return bot;
    }

    @Bean
    // TODO: REFACTOR?
    //  Map command names to command objects for further injecting in CommandManager
    public HashMap<String, CommandHandler<?, ?>> botCommands(List<CommandHandler<?, ?>> commandHandlers) {
        var commandsMap = new HashMap<String, CommandHandler<?, ?>>();
        for (CommandHandler<?, ?> commandHandler : commandHandlers) {
            commandsMap.put(commandHandler.command().command(), commandHandler);
        }
        return commandsMap;
    }


}
