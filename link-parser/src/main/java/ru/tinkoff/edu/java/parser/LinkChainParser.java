package ru.tinkoff.edu.java.parser;

import ru.tinkoff.edu.java.response.BaseResponse;

import java.util.Optional;

public abstract class LinkChainParser implements LinkParser {
    private LinkParser next;

    public static LinkParser chain(LinkChainParser first, LinkChainParser... chain) {
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
