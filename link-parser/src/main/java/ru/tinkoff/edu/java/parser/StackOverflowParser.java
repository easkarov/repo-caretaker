package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowParsingResponse;

import java.util.Optional;
import java.util.regex.Pattern;

public class StackOverflowParser extends LinkChainParser {
    private static final String QID_REGEX = "^https://stackoverflow\\.com/questions/(\\d+)(/|$)";

    @Override
    public Optional<ParsingResponse> parse(String text) {
        var pattern = Pattern.compile(QID_REGEX);
        var matcher = pattern.matcher(text.trim());
        if (matcher.find()) {
            String questionId = matcher.group(1);
            return Optional.of(new StackOverflowParsingResponse(questionId));
        }
        return parseNext(text);
    }
}

