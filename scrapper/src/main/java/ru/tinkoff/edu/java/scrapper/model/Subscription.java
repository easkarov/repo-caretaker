package ru.tinkoff.edu.java.scrapper.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Subscription {
    private Long chatId;
    private Long linkId;
}
