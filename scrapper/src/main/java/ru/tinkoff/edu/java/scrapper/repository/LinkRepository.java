package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.util.List;

public interface LinkRepository {
    List<Link> findAll();
    int add(Link link);
    int remove(long id);
    int bindToChat(Link link, long chatId);
}
