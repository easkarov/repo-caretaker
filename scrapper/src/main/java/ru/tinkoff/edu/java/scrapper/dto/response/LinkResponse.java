package ru.tinkoff.edu.java.scrapper.dto.response;

import lombok.Builder;

import java.net.URI;

@Builder
public record LinkResponse(
        long id,
        URI url
) {
}
