package ru.tinkoff.edu.java.scrapper.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;


@ConfigurationProperties("github-client")
public record GitHubConfiguration(
        @DefaultValue("https://api.github.com") String baseUrl,
        @DefaultValue("2022-11-28") String apiVersion
) {
}
