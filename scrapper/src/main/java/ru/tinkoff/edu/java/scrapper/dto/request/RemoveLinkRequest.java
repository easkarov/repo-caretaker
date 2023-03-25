package ru.tinkoff.edu.java.scrapper.dto.request;

import java.net.URI;

public record RemoveLinkRequest(
        URI link
) {
}
