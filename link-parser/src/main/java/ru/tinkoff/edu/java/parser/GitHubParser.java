package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.response.BaseResponse;
import ru.tinkoff.edu.java.response.GitHubResponse;

import java.util.Optional;
import java.util.regex.Pattern;

public class GitHubParser extends LinkChainParser {
    private static final String USER_REPO_REGEX = "^https://github.com/([\\w.-]+)/([\\w.-]+)(/|$)";

    @Override
    public Optional<BaseResponse> parse(String text) {
        var pattern = Pattern.compile(USER_REPO_REGEX);
        var matcher = pattern.matcher(text);
        if (matcher.find()) {
            String username = matcher.group(1);
            String repository = matcher.group(2);
            return Optional.of(new GitHubResponse(username, repository));
        }
        return parseNext(text);
    }
}
