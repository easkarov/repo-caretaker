package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends BaseRepository<Chat> {
    List<Chat> findAllByLink(long linkId);
}
