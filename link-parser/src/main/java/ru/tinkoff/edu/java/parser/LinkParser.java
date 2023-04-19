package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.parser.response.ParsingResponse;

import java.util.Optional;

public interface LinkParser {
    Optional<ParsingResponse> parse(String text);
}
