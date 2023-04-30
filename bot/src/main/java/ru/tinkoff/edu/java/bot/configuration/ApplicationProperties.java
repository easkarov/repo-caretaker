package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        @NotNull String test,
        @NotNull Bot bot,
        @NotNull ScrapperClient client,
        @NotNull RabbitMQProperties rabbitmq
) {

    public record ScrapperClient(@DefaultValue("https://localhost:8000/api") String url) {
    }

    public record Bot(String token) {
    }

    public record RabbitMQProperties(
            String queue,
            String key,
            String dlq,
            String dlx
    ) {
    }
}