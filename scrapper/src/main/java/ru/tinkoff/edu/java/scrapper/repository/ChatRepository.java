package ru.tinkoff.edu.java.scrapper.repository;


import ru.tinkoff.edu.java.scrapper.model.Chat;

import java.util.List;

public interface ChatRepository {
    List<Chat> findAll();
    int add(Chat link);
    int remove(long id);
}
