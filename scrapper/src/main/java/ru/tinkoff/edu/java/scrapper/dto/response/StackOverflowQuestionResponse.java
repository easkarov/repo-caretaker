package ru.tinkoff.edu.java.scrapper.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record StackOverflowQuestionResponse(
        @JsonProperty("question_id") long id,
        @JsonProperty("last_activity_date")OffsetDateTime updateAt
        ) {
}
