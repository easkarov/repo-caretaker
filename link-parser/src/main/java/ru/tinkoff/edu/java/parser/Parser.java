package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.response.BaseResponse;

import java.util.Optional;

public interface Parser {
    Optional<BaseResponse> parse(String text);
}
