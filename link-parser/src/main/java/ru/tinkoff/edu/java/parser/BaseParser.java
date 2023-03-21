package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.response.BaseResponse;

import java.util.Optional;

public abstract class BaseParser implements Parser {
    private Parser next;

    public static Parser chain(BaseParser first, BaseParser... chain) {
        var head = first;
        for (var nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    protected Optional<BaseResponse> parseNext(String text) {
        return (next == null) ? Optional.empty() : next.parse(text);
    }
}
