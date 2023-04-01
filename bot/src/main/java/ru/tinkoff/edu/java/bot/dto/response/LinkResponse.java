package ru.tinkoff.edu.java.bot.dto.response;

import lombok.Builder;

import java.net.URI;

@Builder
public record LinkResponse(
        long id,
        URI url
) {
}
