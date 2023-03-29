package ru.tinkoff.edu.java.bot.bot;

import com.pengrad.telegrambot.request.BaseRequest;
import lombok.Builder;

import java.util.Optional;

@Builder
public record HandledUpdate(
        Optional<BaseRequest<?, ?>> request,
        State newState
) {
}
