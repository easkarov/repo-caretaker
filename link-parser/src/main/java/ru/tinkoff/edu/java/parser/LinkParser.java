package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.response.BaseResponse;

import java.util.Optional;

public interface LinkParser {
    Optional<BaseResponse> parse(String text);
}
