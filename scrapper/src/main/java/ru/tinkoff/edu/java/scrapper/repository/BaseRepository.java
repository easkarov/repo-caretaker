package ru.tinkoff.edu.java.scrapper.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> { // maybe parametrize ID?
    Optional<T> findById(long id);

    List<T> findAll();

    T save(T entity);

    boolean removeById(long id);
}
