package ru.tinkoff.edu.java.bot.configuration;


import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.UpdatesProcessor;
import ru.tinkoff.edu.java.bot.command.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationConfig applicationConfig;
    private final UpdatesProcessor updatesProcessor;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(applicationConfig.bot().token());
        bot.setUpdatesListener(updatesProcessor);
        updatesProcessor.setBot(bot);
        return bot;
    }

    @Bean
    // Вот этот бин потом можно будет заинжектить в CommandsHandler для более быстрого поиска команды что ли :/
    //  Map command names to command objects for further injecting in CommandManager
    public Map<String, CommandHandler<?, ?>> botCommands(List<CommandHandler<?, ?>> commandHandlers) {
        var commandsMap = new HashMap<String, CommandHandler<?, ?>>();
        for (var commandHandler : commandHandlers) {
            commandsMap.put(commandHandler.command().name(), commandHandler);
        }
        return commandsMap;
    }

}
