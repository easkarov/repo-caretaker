package ru.tinkoff.edu.java.scrapper.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;


// Я хотел сделать конфигурацию таким образом, но в итоге решил не мудрить и бахнул @Value.
// Хотелось бы узнать всё-таки каким-образом в этой ситуации сделать будет лучше.
@ConfigurationProperties("github-client")
public record GitHubConfiguration(
        @DefaultValue("https://api.github.com") String baseUrl,
        @DefaultValue("2022-11-28") String apiVersion
) {
}
