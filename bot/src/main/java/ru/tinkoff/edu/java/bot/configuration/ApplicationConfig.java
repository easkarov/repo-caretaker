package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull String test,
        @NotNull Bot bot,
        @NotNull ScrapperClient client
) {

    public record ScrapperClient(@DefaultValue("https://localhost:8000/api") String url) {}
    public record Bot(String token) {}

}