package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationProperties(
        @NotNull Scheduler scheduler,
        @NotNull Duration linkUpdateAge,
        @NotNull AccessType databaseAccessType,
        @NotNull boolean useQueue,
        @NotNull RabbitMQProperties rabbitmq
) {
    public record Scheduler(Duration interval) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }

    public record RabbitMQProperties(
            @NotBlank String exchange,
            @NotBlank String queue,
            @NotBlank String key,
            @NotBlank String dlx
    ) {
    }
}
