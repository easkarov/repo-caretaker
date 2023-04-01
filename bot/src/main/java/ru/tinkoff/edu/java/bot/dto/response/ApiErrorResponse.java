package ru.tinkoff.edu.java.bot.dto.response;

import lombok.Builder;

import java.util.ArrayList;

@Builder
public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    ArrayList<String> stacktrace
) {
}
