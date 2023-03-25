package ru.tinkoff.edu.java.bot.dto;

import java.net.URI;
import java.util.ArrayList;


public record LinkUpdate(
    long id,
    URI url,
    String description,
    ArrayList<Long> tgChatIds
) {
}
