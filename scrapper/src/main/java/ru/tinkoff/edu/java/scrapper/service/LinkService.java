package ru.tinkoff.edu.java.scrapper.service;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.util.List;


public interface LinkService {
    Link track(long tgChatId, String url);
    Link untrack(long tgChatId, String url);
    List<Link> listAll(long tgChatId);
}
