package ru.tinkoff.edu.java.scrapper.repository;

import ru.tinkoff.edu.java.scrapper.model.Subscription;

import java.util.List;

public interface SubscriptionRepository {
    List<Subscription> findAll();
    int remove(long chatId, long linkId);
    int add(Subscription subscription);
}
