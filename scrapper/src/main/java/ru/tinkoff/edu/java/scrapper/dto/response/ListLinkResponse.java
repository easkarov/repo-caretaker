package ru.tinkoff.edu.java.scrapper.dto.response;

import java.util.List;

public record ListLinkResponse(
        List<LinkResponse> links,
        int size
) {
}
