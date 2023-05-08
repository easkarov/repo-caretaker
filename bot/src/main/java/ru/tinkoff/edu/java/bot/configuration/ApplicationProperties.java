package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        @NotNull String test,
        @NotNull Bot bot,
        @NotNull ScrapperClient client,
        @NotNull RabbitMQProperties rabbitmq
) {

    public record ScrapperClient(@NotBlank String url) {
    }

    public record Bot(@NotBlank String token) {
    }

    public record RabbitMQProperties(
            @NotBlank String queue,
            @NotBlank String key,
            @NotBlank String dlq,
            @NotBlank String dlx
    ) {
    }
}