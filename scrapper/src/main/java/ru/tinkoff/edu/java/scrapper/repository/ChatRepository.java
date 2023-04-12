package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    List<Chat> findAll();
    List<Chat> findAllByLink(long linkId);
    Optional<Chat> findById(long id);
    Chat save(Chat link);
    boolean removeById(long id);
}
