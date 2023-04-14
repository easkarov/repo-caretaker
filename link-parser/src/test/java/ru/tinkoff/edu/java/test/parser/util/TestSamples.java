package ru.tinkoff.edu.java.test.parser.util;

import org.junit.jupiter.params.provider.Arguments;
import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;

import java.util.stream.Stream;

public class TestSamples {
    private final static String GITHUB_URL = "https://github.com/%s/%s";
    private final static String GITHUB_SLASH_URL = GITHUB_URL + "/";

    private final static String URL = "https://stackoverflow.com/questions/%s";
    private final static String SLASH_URL = URL + "/";

    public static Stream<Arguments> provideGitHubValidLinks() {

        Stream<GitHubParsingResponse> input = Stream.of(
                new GitHubParsingResponse("user", "repo"),
                new GitHubParsingResponse("123", "123"),
                new GitHubParsingResponse("a-b-c", "a.b.c"),
                new GitHubParsingResponse("dot.dot.dot", "s-s-s")
        );

        return input.flatMap(response -> Stream.of(
                Arguments.of(GITHUB_URL.formatted(response.user(), response.repo()),
                        response.user(), response.repo()),
                Arguments.of(GITHUB_SLASH_URL.formatted(response.user(), response.repo()),
                        response.user(), response.repo()))
        );
    }

    public static Stream<Arguments> provideStackOverflowValidLinks() {
        Stream<String> questions = Stream.of("12356689", "945987459", "0");

        return questions.flatMap(question -> Stream.of(
                Arguments.of(URL.formatted(question), question),
                Arguments.of(SLASH_URL.formatted(question), question)));
    }

    public static Stream<String> provideInvalidLinks() {
        return Stream.of(
                "http://github.com/emil/project",
                "https://stackoverflow.com/questions/",
                "https://stackoverflow.com/questions/      ",
                "https://stackoverflow.com/questions/aaaa",
                "https://stackoverflow.com/questions/123 123",
                "https://vk.com/user/repo",
                "http://github.com/emil/project   ",
                "https://github.co/emil/project",
                "github.com/user/repo",
                "https://github.com//",
                "https://github.com/ /repo",
                "https://github.com/u s e r/repo",
                "https://vk.com/user/repo"
        );
    }

}
