package ru.tinkoff.edu.java.scrapper.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.LinkChainParser;
import ru.tinkoff.edu.java.parser.LinkParser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;
import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowParsingResponse;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkUpdater implements Updater {

    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Value("#{@linkUpdateAge}")
    private final Duration updateAge;

    @Transactional
    @Override
    public void update() {
        ArrayList<Link> updatedLinks = new ArrayList<>();

        for (var link : linkRepository.findLongUpdated(updateAge)) {
            var parsingResult = parseUrl(link.getUrl());

            if (parsingResult.isEmpty()) continue;

            switch (parsingResult.get()) {
                case StackOverflowParsingResponse r -> {
                    processStackOverflowLink(link, r).ifPresent(updatedLinks::add);
                }
                case GitHubParsingResponse r -> {
                    processGitHubLink(link, r).ifPresent(updatedLinks::add);
                }
            }
        }

        updatedLinks.forEach(linkRepository::save);
        notifyBot(updatedLinks);
    }

    public Optional<Link> processGitHubLink(Link link, GitHubParsingResponse r) {
        var response = gitHubClient.fetchRepository(r.user(), r.repo());
        if (response.isPresent() && !link.getUpdatedAt().equals(response.get().updatedAt())) {
            link.setUpdatedAt(response.get().updatedAt());
            return Optional.of(link);
        }
        return Optional.empty();
    }

    public Optional<Link> processStackOverflowLink(Link link, StackOverflowParsingResponse r) {
        var response = stackOverflowClient.fetchQuestion(r.questionId());
        if (response.isPresent() && !link.getUpdatedAt().equals(response.get().updatedAt())) {
            link.setUpdatedAt(response.get().updatedAt());
            return Optional.of(link);
        }
        return Optional.empty();
    }

    public Optional<ParsingResponse> parseUrl(String url) {
        LinkParser parser = LinkChainParser.chain(new GitHubParser(), new StackOverflowParser());
        return parser.parse(url);
    }

    public void notifyBot(List<Link> links) {
        for (var link : links) {
            List<Chat> linkChats = chatRepository.findAllByLink(link.getId());
            LinkUpdate update = LinkUpdate.builder()
                    .id(link.getId())
                    .description("Link has been updated")
                    .tgChatIds(linkChats.stream().map(Chat::getId).toList())
                    .url(URI.create(link.getUrl()))
                    .build();
            botClient.sendUpdate(update);
        }
    }
}
