package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAll();
    List<Link> findAllByChat(long chatId);
    Optional<Link> findByUrl(String url);
    Optional<Link> findById(long id);
    List<Link> findLeastRecentlyUpdated(TemporalAmount delta);
    Link save(Link link);
    boolean removeById(long id);
    boolean removeFromChat(long chatId, long linkId);
    boolean addToChat(long chatId, long linkId);
}
