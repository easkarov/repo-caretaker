package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.util.List;

public interface ChatRepository extends BaseRepository<Chat> {
    List<Chat> findAllByLink(Link link);
}
