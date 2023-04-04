package ru.tinkoff.edu.java.bot.dto;

import com.pengrad.telegrambot.request.BaseRequest;
import lombok.Builder;
import ru.tinkoff.edu.java.bot.enums.State;

import java.util.Optional;

@Builder
public record HandledUpdate(
        Optional<BaseRequest<?, ?>> request,
        State newState
) {
    public static final HandledUpdate EMPTY = new HandledUpdate(Optional.empty(), State.NONE);
}
