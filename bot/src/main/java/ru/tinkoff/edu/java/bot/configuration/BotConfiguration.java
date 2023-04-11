package ru.tinkoff.edu.java.bot.configuration;


import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.command.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(applicationConfig.bot().token());
    }
}
