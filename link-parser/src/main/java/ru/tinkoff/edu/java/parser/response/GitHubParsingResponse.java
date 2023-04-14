package ru.tinkoff.edu.java.parser.response;

public record GitHubParsingResponse(String user, String repo) implements ParsingResponse {
}
