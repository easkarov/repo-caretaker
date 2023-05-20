package ru.tinkoff.edu.java.scrapper.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final ApplicationProperties applicationProperties;

    @Bean
    public Duration linkUpdateAge() {
        return applicationProperties.linkUpdateAge();
    }

    @Bean
    public long linkUpdateSchedulerIntervalMs() {
        return applicationProperties.scheduler().interval().toMillis();
    }

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
