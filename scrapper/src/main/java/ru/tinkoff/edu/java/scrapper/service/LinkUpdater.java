package ru.tinkoff.edu.java.scrapper.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.LinkChainParser;
import ru.tinkoff.edu.java.parser.LinkParser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowParsingResponse;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.GithubUpdateCriteria;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.JdbcLinkRepository;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkUpdater {

    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;

    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Value("#{@linkUpdateAge}")
    private final Duration updateAge;


    // needs decomposing
    @Transactional
    public void update() throws JsonProcessingException {
        ArrayList<Link> updatedLinks = new ArrayList<>();

        for (var link : linkRepository.findLongUpdated(updateAge)) {
            var parsingResult = parseUrl(link.getUrl());

            if (parsingResult.isEmpty()) continue;

            switch (parsingResult.get()) {
                case StackOverflowParsingResponse r -> {
                    var response = stackOverflowClient.fetchQuestion(r.questionId());
                    if (response.isPresent() && !link.getUpdatedAt().equals(response.get().updatedAt())) {
                        link.setUpdatedAt(response.get().updatedAt());
                        updatedLinks.add(link);
                    }
                }
                case GitHubParsingResponse r -> {
                    var curCommitsNumber = gitHubClient.fetchCommitsNumber(r.user(), r.repo());
                    if (curCommitsNumber.isEmpty()) continue;

                    var objectMapper = new ObjectMapper();
                    var githubCriteria = objectMapper.readValue(link.getUpdateData(), GithubUpdateCriteria.class);

                    Integer dbCommitsNumber = githubCriteria.commitsNumber();

                    if (dbCommitsNumber == null) {
                        githubCriteria.commitsNumber(curCommitsNumber.get());
                        link.setUpdatedAt(OffsetDateTime.now());
                        link.setUpdateData(objectMapper.writeValueAsString(githubCriteria));
                    } else if (dbCommitsNumber.equals(curCommitsNumber.get())) {
                        githubCriteria.commitsNumber(curCommitsNumber.get());
                        link.setUpdatedAt(OffsetDateTime.now());
                        link.setUpdateData(objectMapper.writeValueAsString(githubCriteria));
                        updatedLinks.add(link);
                    }
                }
            }
        }

        updatedLinks.forEach(linkRepository::save);
        notifyBot(updatedLinks);
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
