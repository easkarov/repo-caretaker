package ru.tinkoff.edu.java.bot.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    ApplicationConfig applicationConfig;
    @Bean
    public ScrapperClient stackOverflowClient() {
        // Создаётся клиент с базовой URL из файла конфига (есть значение по умолчанию @DefaultValue)
        return new ScrapperClient(applicationConfig.client().url());
    }

}
