package ru.tinkoff.edu.java.bot.dto.response;

import java.util.List;

public record ListLinkResponse(
        List<LinkResponse> links,
        int size
) {
}
