package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.response.BaseResponse;
import ru.tinkoff.edu.java.response.StackOverflowResponse;

import java.util.Optional;
import java.util.regex.Pattern;

public class StackOverflowParser extends LinkChainParser {
    private static final String QID_REGEX = "^https://stackoverflow\\.com/questions/(\\d+)(/|$)";

    @Override
    public Optional<BaseResponse> parse(String text) {
        var pattern = Pattern.compile(QID_REGEX);
        var matcher = pattern.matcher(text);
        if (matcher.find()) {
            String questionId = matcher.group(1);
            return Optional.of(new StackOverflowResponse(questionId));
        }
        return parseNext(text);
    }
}

