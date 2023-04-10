package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.util.List;

public interface LinkRepository {
    List<Link> findAll();
    Link add(Link link);
    boolean remove(long id);
    boolean removeFromChat(long linkId, long chatId);
    boolean addToChat(long linkId, long chatId);
}
