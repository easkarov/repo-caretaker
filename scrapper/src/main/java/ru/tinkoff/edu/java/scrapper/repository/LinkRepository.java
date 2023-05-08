package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository extends BaseRepository<Link> {
    List<Link> findAllByChat(Chat chatId);

    Optional<Link> findByUrl(String url);

    List<Link> findLeastRecentlyUpdated(OffsetDateTime oldThanDateTime);

    boolean removeFromChat(Chat chat, Link link);

    boolean addToChat(Chat chat, Link link);
}
