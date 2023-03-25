package ru.tinkoff.edu.java.scrapper.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;


// Я хотел сделать конфигурацию таким образом, но в итоге решил не мудрить и бахнул @Value.
// Хотелось бы узнать всё-таки каким-образом в этой ситуации сделать будет лучше.
@ConfigurationProperties("stackoverflow-client")
public record StackOverflowConfiguration(
        @DefaultValue("https://api.stackexchange.com/2.3") String baseUrl
) {
}
