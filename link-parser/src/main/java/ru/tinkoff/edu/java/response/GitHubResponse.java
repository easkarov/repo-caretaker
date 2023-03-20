package ru.tinkoff.edu.java.response;

public record GitHubResponse(String user, String repo) implements BaseResponse {
}
