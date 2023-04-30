package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;
import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowParsingResponse;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.util.List;
import java.util.Optional;

import static java.util.Map.Entry;

public interface LinkUpdater {
    void update();

    Optional<Entry<Link, String>> processGitHubLink(Link link, GitHubParsingResponse response);

    Optional<Entry<Link, String>> processStackOverflowLink(Link link, StackOverflowParsingResponse response);

    void notifyBot(List<Entry<Link, String>> links);

    Optional<ParsingResponse> parseUrl(String url);
}
