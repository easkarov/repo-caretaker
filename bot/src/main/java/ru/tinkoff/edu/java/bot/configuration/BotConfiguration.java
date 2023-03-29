package ru.tinkoff.edu.java.bot.configuration;


import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.bot.CommandManager;
import ru.tinkoff.edu.java.bot.bot.Sender;
import ru.tinkoff.edu.java.bot.bot.UpdatesProcessor;
import ru.tinkoff.edu.java.bot.bot.command.Command;
import ru.tinkoff.edu.java.bot.bot.command.LinkCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationConfig applicationConfig;
    private final UpdatesProcessor updatesProcessor;

    @Bean
    public TelegramBot telegramBot(CommandManager manager) {
        TelegramBot bot = new TelegramBot(applicationConfig.botToken());
        bot.setUpdatesListener(updatesProcessor);
        updatesProcessor.setBot(bot);
        System.out.println(manager.commands);
//        System.out.println(manager.helpCommand);
        return bot;
    }

    @Bean
    // TODO: REFACTOR?
    //  Map command names to command objects for further injecting in CommandManager
    public HashMap<String, Command<?, ?>> botCommands(List<Command<?, ?>> commands) {
        var commandsMap = new HashMap<String, Command<?, ?>>();
        for (Command<?, ?> command : commands) {
            commandsMap.put(command.name(), command);
        }
        return commandsMap;
    }


}
