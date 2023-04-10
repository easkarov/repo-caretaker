package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    List<Chat> findAll();
    Optional<Chat> findById(long id);
    Chat add(Chat link);
    boolean remove(long id);
}
