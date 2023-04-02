package ru.tinkoff.edu.java.test.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.response.BaseResponse;
import ru.tinkoff.edu.java.parser.response.GitHubResponse;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.stream.Stream;

public class GithubParserTest {
    GitHubParser gitHubParser = new GitHubParser();

    @ParameterizedTest
    @MethodSource("provideValidLinks")
    void parse_returnGitHubResponse_ValidURLs(String url, String expectedUser, String expectedRepo) {
        // given

        // when
        Optional<BaseResponse> response = gitHubParser.parse(url);

        // then
        assertThat(response).isNotEmpty();
        assertThat(response.get()).isInstanceOf(GitHubResponse.class);

        var githubResponse = (GitHubResponse) response.get();
        assertAll("Validate user and repo in response",
                () -> assertThat(githubResponse.user()).isEqualTo(expectedUser),
                () -> assertThat(githubResponse.repo()).isEqualTo(expectedRepo));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLinks")
    void parse_returnEmptyOptional_InvalidURLs(String url) {
        // given

        // when
        Optional<BaseResponse> response = gitHubParser.parse(url);

        // then
        assertThat(response).isEmpty();
    }

    static Stream<Arguments> provideValidLinks() {
        return TestSamples.provideGitHubValidLinks();
    }

    static Stream<String> provideInvalidLinks() {
        return Stream.of(
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
