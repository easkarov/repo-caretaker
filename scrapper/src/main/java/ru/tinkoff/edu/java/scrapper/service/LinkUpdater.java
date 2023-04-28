package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;
import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowParsingResponse;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.net.URI;
import java.util.Optional;

import static java.util.Map.Entry;

public interface LinkUpdater {
    void update();

    Optional<Entry<Link, String>> processGitHubLink(Link link, GitHubParsingResponse response);

    Optional<Entry<Link, String>> processStackOverflowLink(Link link, StackOverflowParsingResponse response);

    Optional<ParsingResponse> parseUrl(String url);

    default LinkUpdate convertToUpdate(Entry<Link, String> pair) {
        var link = pair.getKey();
        var description = pair.getValue();
        return LinkUpdate.builder()
                .id(link.getId())
                .description(description)
                .tgChatIds(link.getChats().stream().map(Chat::getId).toList())
                .url(URI.create(link.getUrl()))
                .build();
    }
}
