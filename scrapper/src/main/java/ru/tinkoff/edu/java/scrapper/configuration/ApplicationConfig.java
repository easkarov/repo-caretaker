package ru.tinkoff.edu.java.scrapper.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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

}
