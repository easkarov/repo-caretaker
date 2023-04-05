package ru.tinkoff.edu.java.parser.response;

public record GitHubResponse(String user, String repo) implements BaseResponse {
}
