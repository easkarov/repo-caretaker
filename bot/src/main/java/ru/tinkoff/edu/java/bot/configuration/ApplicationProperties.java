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

    public record ScrapperClient(@NotNull String url) {
    }

    public record Bot(@NotNull String token) {
    }

    public record RabbitMQProperties(
            @NotNull String queue,
            @NotNull String key,
            @NotNull String dlq,
            @NotNull String dlx
    ) {
    }
}