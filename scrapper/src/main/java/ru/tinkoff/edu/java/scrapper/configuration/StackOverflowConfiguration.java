package ru.tinkoff.edu.java.scrapper.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;


@ConfigurationProperties("stackoverflow-client")
public record StackOverflowConfiguration(
        @DefaultValue("https://api.stackexchange.com/2.3") String baseUrl
) {
}
