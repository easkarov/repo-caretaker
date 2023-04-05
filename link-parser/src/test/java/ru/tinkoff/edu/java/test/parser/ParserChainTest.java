package ru.tinkoff.edu.java.test.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.parser.GitHubParser;
import ru.tinkoff.edu.java.parser.LinkChainParser;
import ru.tinkoff.edu.java.parser.LinkParser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.parser.response.BaseResponse;
import ru.tinkoff.edu.java.parser.response.GitHubResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ParserChainTest {

    LinkParser linkParser = LinkChainParser.chain(new GitHubParser(), new StackOverflowParser());

    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideGitHubValidLinks")
    void parse_returnGitHubResponse_ValidURLs(String url, String expectedUser, String expectedRepo) {
        // given

        // when
        Optional<BaseResponse> response = linkParser.parse(url);

        // then
        assertThat(response).isNotEmpty();
        assertThat(response.get()).isInstanceOf(GitHubResponse.class);

        var githubResponse = (GitHubResponse) response.get();
        assertAll("Validate user and repo in response",
                () -> assertThat(githubResponse.user()).isEqualTo(expectedUser),
                () -> assertThat(githubResponse.repo()).isEqualTo(expectedRepo));
    }


    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideStackOverflowValidLinks")
    void parse_returnStackOverflowResponse_ValidURLs(String url, String expectedQuestionId) {
        // given

        // when
        Optional<BaseResponse> response = linkParser.parse(url);

        // then
        assertThat(response).isNotEmpty();
        assertThat(response.get()).isInstanceOf(StackOverflowResponse.class);

        var sofResponse = (StackOverflowResponse) response.get();
        assertAll("Validate question id in response",
                () -> assertThat(sofResponse.questionId()).isEqualTo(expectedQuestionId),
                () -> assertThat(sofResponse.questionId()).isEqualTo(expectedQuestionId));
    }

    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideInvalidLinks")
    void parse_returnEmptyOptional_InvalidURLs(String url) {
        // given

        // when
        Optional<BaseResponse> response = linkParser.parse(url);

        // then
        assertThat(response).isEmpty();
    }
}
