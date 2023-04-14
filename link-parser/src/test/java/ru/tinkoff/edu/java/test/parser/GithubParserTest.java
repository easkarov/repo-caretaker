package ru.tinkoff.edu.java.test.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.GitHubParsingResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GithubParserTest {
    GitHubParser gitHubParser = new GitHubParser();

    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideGitHubValidLinks")
    void parse_returnGitHubResponse_ValidURLs(String url, String expectedUser, String expectedRepo) {
        // given

        // when
        Optional<ParsingResponse> response = gitHubParser.parse(url);

        // then
        assertThat(response).isNotEmpty();
        assertThat(response.get()).isInstanceOf(GitHubParsingResponse.class);

        var githubResponse = (GitHubParsingResponse) response.get();
        assertAll("Validate user and repo in response",
                () -> assertThat(githubResponse.user()).isEqualTo(expectedUser),
                () -> assertThat(githubResponse.repo()).isEqualTo(expectedRepo));
    }

    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideInvalidLinks")
    void parse_returnEmptyOptional_InvalidURLs(String url) {
        // given

        // when
        Optional<ParsingResponse> response = gitHubParser.parse(url);

        // then
        assertThat(response).isEmpty();
    }
}
