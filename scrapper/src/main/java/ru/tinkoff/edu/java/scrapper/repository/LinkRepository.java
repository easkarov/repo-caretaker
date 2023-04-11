package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll(long chatId);
    Optional<Link> findByUrl(String url);
    Link add(Link link);
    boolean remove(long id);
    boolean removeFromChat(long chatId, long linkId);
    boolean addToChat(long chatId, long linkId);
}
