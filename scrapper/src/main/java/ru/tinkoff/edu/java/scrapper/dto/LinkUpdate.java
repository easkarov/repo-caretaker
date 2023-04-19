package ru.tinkoff.edu.java.scrapper.dto;

import lombok.Builder;

import java.net.URI;
import java.util.List;


@Builder
public record LinkUpdate(
    long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {
}
