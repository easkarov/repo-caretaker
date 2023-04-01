package ru.tinkoff.edu.java.bot.configuration;


import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.UpdatesProcessor;

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

}
