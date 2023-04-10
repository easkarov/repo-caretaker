package ru.tinkoff.edu.java.scrapper.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Subscription {
    @NotNull
    private Long chatId;
    @NotNull
    private Long linkId;
}
