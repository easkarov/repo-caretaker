package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;

import java.util.Optional;
import java.util.regex.Pattern;

public class GitHubParser extends LinkChainParser {
    private static final String USER_REPO_REGEX = "^https://github.com/([\\w.-]+)/([\\w.-]+)(/$|$)";

    @Override
    public Optional<ParsingResponse> parse(String text) {
        var pattern = Pattern.compile(USER_REPO_REGEX);
        var matcher = pattern.matcher(text.trim());
        if (matcher.find()) {
            String username = matcher.group(1);
            String repository = matcher.group(2);
            return Optional.of(new GitHubParsingResponse(username, repository));
        }
        return parseNext(text);
    }
}
