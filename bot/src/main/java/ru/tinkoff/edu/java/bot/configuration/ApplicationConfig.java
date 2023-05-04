package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Bot bot,
        @NotNull ScrapperClient client
) {

    public record ScrapperClient(@NotNull String url) {
    }

    public record Bot(@NotNull String token) {
    }
}