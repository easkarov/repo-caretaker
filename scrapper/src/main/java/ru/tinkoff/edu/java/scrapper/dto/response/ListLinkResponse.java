package ru.tinkoff.edu.java.scrapper.dto.response;

import java.util.ArrayList;

public record ListLinkResponse(
        ArrayList<LinkResponse> links,
        int size
) {
}
