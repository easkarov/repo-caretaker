package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;

public interface LinkRepository extends BaseRepository<Link> {
    List<Link> findAllByChat(long chatId);
    Optional<Link> findByUrl(String url);
    List<Link> findLeastRecentlyUpdated(OffsetDateTime oldThan);
    boolean removeFromChat(long chatId, long linkId);
    boolean addToChat(long chatId, long linkId);
}
