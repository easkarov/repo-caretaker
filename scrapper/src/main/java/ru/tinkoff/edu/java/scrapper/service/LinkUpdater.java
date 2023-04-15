package ru.tinkoff.edu.java.scrapper.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import ru.tinkoff.edu.java.scrapper.model.GithubUpdateData;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import javax.swing.text.html.Option;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        ArrayList<Map.Entry<Link, String>> updatedLinks = new ArrayList<>();

        for (var link : linkRepository.findLongUpdated(updateAge)) {
            var parsingResult = parseUrl(link.getUrl());

            if (parsingResult.isEmpty()) continue;

            switch (parsingResult.get()) {
                case StackOverflowParsingResponse r -> processSOFLink(link, r).ifPresent(updatedLinks::add);
                case GitHubParsingResponse r -> processGitHubLink(link, r).ifPresent(updatedLinks::add);
            }
        }

        updatedLinks.forEach(pair -> linkRepository.save(pair.getKey()));
        notifyBot(updatedLinks);
    }

    public Optional<Map.Entry<Link, String>> processSOFLink(Link link, StackOverflowParsingResponse response) {
        var updateDescriptions = new ArrayList<String>();

        // check if something was updated
        var question = stackOverflowClient.fetchQuestion(response.questionId());
        if (question.isPresent() && !link.getUpdatedAt().equals(question.get().updatedAt())) {
            link.setUpdatedAt(question.get().updatedAt());
            updateDescriptions.add("SOF Link has been updated!");
        }

        if (updateDescriptions.size() == 0)
            return Optional.empty();

        return Optional.of(Map.entry(link, String.join("\n", updateDescriptions)));
    }

    @SneakyThrows
    public Optional<Map.Entry<Link, String>> processGitHubLink(Link link, GitHubParsingResponse response) {
        var mapper = new ObjectMapper();
        var updateData = mapper.readValue(link.getUpdateData(), GithubUpdateData.class);
        var updateDescriptions = new ArrayList<String>();

        // check on new commits
        var curCommitsNumber = gitHubClient.fetchCommitsNumber(response.user(), response.repo());
        if (curCommitsNumber.isPresent()) {
            Integer dbCommitsNumber = updateData.getCommitsNumber();
            if (dbCommitsNumber == null || !dbCommitsNumber.equals(curCommitsNumber.get())) {
                updateData.setCommitsNumber(curCommitsNumber.get());
                link.setUpdatedAt(OffsetDateTime.now());
                updateDescriptions.add("Detected new commits on repository!");
            }
        }

        if (updateDescriptions.size() == 0) {
            return Optional.empty();
        }

        link.setUpdateData(mapper.writeValueAsString(updateData));
        return Optional.of(Map.entry(link, String.join("\n", updateDescriptions)));
    }


    public Optional<ParsingResponse> parseUrl(String url) {
        LinkParser parser = LinkChainParser.chain(new GitHubParser(), new StackOverflowParser());
        return parser.parse(url);
    }

    public void notifyBot(List<Map.Entry<Link, String>> linkPairs) {
        for (var pair : linkPairs) {
            var link = pair.getKey();
            List<Chat> linkChats = chatRepository.findAllByLink(link.getId());
            LinkUpdate update = LinkUpdate.builder()
                    .id(link.getId())
                    .description(pair.getValue())
                    .tgChatIds(linkChats.stream().map(Chat::getId).toList())
                    .url(URI.create(link.getUrl()))
                    .build();
            botClient.sendUpdate(update);
        }
    }
}
