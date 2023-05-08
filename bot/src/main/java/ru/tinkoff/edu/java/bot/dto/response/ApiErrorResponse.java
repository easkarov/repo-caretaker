package ru.tinkoff.edu.java.bot.dto.response;

import java.util.ArrayList;
import lombok.Builder;

@Builder
public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    ArrayList<String> stacktrace
) {
}
