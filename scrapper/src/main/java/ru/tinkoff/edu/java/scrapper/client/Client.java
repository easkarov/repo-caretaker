package ru.tinkoff.edu.java.scrapper.client;

import java.util.Optional;


// ??? Подскажи, насколько вообще разумно так делать. Добавил для присутствия абстракции,
// можно было конечно просто маркерным сделать :/
public interface Client {
    <T> Optional<T> get(String uri, Class<T> destClass);
}
