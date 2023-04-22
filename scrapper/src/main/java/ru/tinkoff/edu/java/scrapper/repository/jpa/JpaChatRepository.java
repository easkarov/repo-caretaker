package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository {

    private static final String SELECT_ALL = """
            SELECT c FROM Chat c
            """;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Chat> findById(long id) {
        return Optional.ofNullable(entityManager.find(Chat.class, id));
    }

    @Override
    public List<Chat> findAll() {
        TypedQuery<Chat> query = entityManager.createQuery(SELECT_ALL, Chat.class);
        return query.getResultList();
    }

    @Override
    public Chat save(Chat entity) {
        return entityManager.merge(entity);
    }

    @Override
    public boolean removeById(long id) {
        var chat = findById(id);
        if (chat.isEmpty()) return false;
        entityManager.remove(chat.get());
        return true;
    }

    @Override
    public List<Chat> findAllByLink(Link link) {
        return link.getChats().stream().toList();
    }
}
