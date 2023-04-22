package ru.tinkoff.edu.java.scrapper.service.jpa;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import ru.tinkoff.edu.java.scrapper.model.SOFUpdateData;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Map.Entry;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaLinkUpdater implements LinkUpdater {

    private final JpaLinkRepository linkRepository;

    private final BotClient botClient;
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    @Value("#{@linkUpdateAge}")
    private final Duration updateAge;

    private final ObjectMapper mapper;


    @Transactional
    @Override
    public void update() {
        ArrayList<Entry<Link, String>> updatedLinks = new ArrayList<>();

        for (var link : linkRepository.findAllByUpdatedAtBefore(OffsetDateTime.now().minus(updateAge))) {
            var parsingResult = parseUrl(link.getUrl());

            if (parsingResult.isEmpty()) continue;

            switch (parsingResult.get()) {
                case StackOverflowParsingResponse r -> processStackOverflowLink(link, r).ifPresent(updatedLinks::add);
                case GitHubParsingResponse r -> processGitHubLink(link, r).ifPresent(updatedLinks::add);
            }
        }

        updatedLinks.forEach(pair -> linkRepository.save(pair.getKey()));
        notifyBot(updatedLinks);
    }

    @SneakyThrows
    @Override
    public Optional<Entry<Link, String>> processStackOverflowLink(Link link, StackOverflowParsingResponse response) {
        var updateData = mapper.readValue(link.getUpdateData(), SOFUpdateData.class);
        var updateDescriptions = new ArrayList<String>();

        // check if question was updated
        var question = stackOverflowClient.fetchQuestion(response.questionId());
        if (question.isPresent()) {
            var updatedAt = updateData.getUpdatedAt();
            if (updatedAt == null || updatedAt.equals(question.get().updatedAt())) {
                updateData.setUpdatedAt(question.get().updatedAt());
                link.setUpdatedAt(OffsetDateTime.now());
                updateDescriptions.add("* SOF question has been updated!");
            }
        }

        if (updateDescriptions.size() == 0)
            return Optional.empty();

        return Optional.of(Map.entry(link, String.join("\n", updateDescriptions)));
    }

    @SneakyThrows
    @Override
    public Optional<Entry<Link, String>> processGitHubLink(Link link, GitHubParsingResponse response) {
        var updateData = mapper.readValue(link.getUpdateData(), GithubUpdateData.class);
        var updateDescriptions = new ArrayList<String>();

        var repository = gitHubClient.fetchRepository(response.user(), response.repo());
        if (repository.isPresent()) {
            // check if repository was updated generally
            var updatedAt = updateData.getUpdatedAt();
            if (updatedAt == null || !updatedAt.equals(repository.get().updatedAt())) {
                updateData.setUpdatedAt(repository.get().updatedAt());
                updateDescriptions.add("* Repository has been updated!");
            }
            // check on open issues count
            var openIssues = updateData.getOpenIssues();
            if (openIssues == null || !openIssues.equals(repository.get().openIssues())) {
                updateData.setOpenIssues(repository.get().openIssues());
                updateDescriptions.add("* Open issues count has been updated!");
            }
        }

        // check on new commits
        var curCommitsNumber = gitHubClient.fetchCommitsNumber(response.user(), response.repo());
        if (curCommitsNumber.isPresent()) {
            var dbCommitsNumber = updateData.getCommitsNumber();
            if (dbCommitsNumber == null || !dbCommitsNumber.equals(curCommitsNumber.get())) {
                updateData.setCommitsNumber(curCommitsNumber.get());
                updateDescriptions.add("* Detected new commits on repository!");
            }
        }

        if (updateDescriptions.size() == 0) {
            return Optional.empty();
        }

        link.setUpdatedAt(OffsetDateTime.now());
        link.setUpdateData(mapper.writeValueAsString(updateData));

        return Optional.of(Map.entry(link, String.join("\n", updateDescriptions)));
    }


    @Override
    public Optional<ParsingResponse> parseUrl(String url) {
        LinkParser parser = LinkChainParser.chain(new GitHubParser(), new StackOverflowParser());
        return parser.parse(url);
    }


    @Override
    public void notifyBot(List<Entry<Link, String>> linkPairs) {
        for (var pair : linkPairs) {
            var link = pair.getKey();
            LinkUpdate update = LinkUpdate.builder()
                    .id(link.getId())
                    .description(pair.getValue())
                    .tgChatIds(link.getChats().stream().map(Chat::getId).toList())
                    .url(URI.create(link.getUrl()))
                    .build();
            botClient.sendUpdate(update);
        }
    }
}
