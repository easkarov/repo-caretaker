package ru.tinkoff.edu.java.test.parser;

import org.junit.jupiter.params.provider.Arguments;
import ru.tinkoff.edu.java.parser.response.GitHubResponse;

import java.util.stream.Stream;

public class TestSamples {
    private final static String GITHUB_URL = "https://github.com/%s/%s";
    private final static String GITHUB_SLASH_URL = GITHUB_URL + "/";

    private final static String URL = "https://stackoverflow.com/questions/%s";
    private final static String SLASH_URL = URL + "/";

    static Stream<Arguments> provideGitHubValidLinks() {

        Stream<GitHubResponse> input = Stream.of(
                new GitHubResponse("user", "repo"),
                new GitHubResponse("123", "123"),
                new GitHubResponse("a-b-c", "a.b.c"),
                new GitHubResponse("dot.dot.dot", "s-s-s")
        );

        return input.flatMap(response -> Stream.of(
                Arguments.of(GITHUB_URL.formatted(response.user(), response.repo()),
                        response.user(), response.repo()),
                Arguments.of(GITHUB_SLASH_URL.formatted(response.user(), response.repo()),
                        response.user(), response.repo()))
        );
    }

    static Stream<Arguments> provideStackOverflowValidLinks() {

        Stream<String> questions = Stream.of("12356689", "945987459", "0");

        return questions.flatMap(question -> Stream.of(
                Arguments.of(URL.formatted(question), question),
                Arguments.of(SLASH_URL.formatted(question), question)));
    }

}
