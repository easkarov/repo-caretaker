package ru.tinkoff.edu.java.test.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.parser.response.ParsingResponse;
import ru.tinkoff.edu.java.parser.response.StackOverflowParsingResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StackOverflowParserTest {
    StackOverflowParser stackOverflowParser = new StackOverflowParser();

    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideStackOverflowValidLinks")
    void parse_returnStackOverflowResponse_ValidURLs(String url, String expectedQuestionId) {
        // given

        // when
        Optional<ParsingResponse> response = stackOverflowParser.parse(url);
        // then
        assertThat(response).isNotEmpty();
        assertThat(response.get()).isInstanceOf(StackOverflowParsingResponse.class);

        var sofResponse = (StackOverflowParsingResponse) response.get();
        assertAll("Validate question id in response",
                () -> assertThat(sofResponse.questionId()).isEqualTo(expectedQuestionId),
                () -> assertThat(sofResponse.questionId()).isEqualTo(expectedQuestionId));
    }

    @ParameterizedTest
    @MethodSource("ru.tinkoff.edu.java.test.parser.util.TestSamples#provideInvalidLinks")
    void parse_returnEmptyOptional_InvalidURLs(String url) {
        // given

        // when
        Optional<ParsingResponse> response = stackOverflowParser.parse(url);

        // then
        assertThat(response).isEmpty();
    }


}
