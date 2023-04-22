package ru.tinkoff.edu.java.scrapper.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class JpaLinkRepository implements LinkRepository {

    private final static String SELECT_ALL = """
            SELECT l from Link l
            """;

    private final static String SELECT_BY_URL = """
            SELECT l from Link l WHERE url = :url
            """;

    private final static String SELECT_LEAST_RECENTLY_UPDATED = """
            SELECT l from Link l WHERE updatedAt < :date
            """;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Link> findById(long id) {
        return Optional.ofNullable(entityManager.find(Link.class, id));
    }

    @Override
    public List<Link> findAll() {
        return entityManager.createQuery(SELECT_ALL, Link.class).getResultList();
    }

    @Override
    public Link save(Link entity) {
        log.info(entity.getUpdateData());
        if (entity.getId() == null) {
            entityManager.persist(entity);
            return entity;
        }
        return entityManager.merge(entity);
    }

    @Override
    public boolean removeById(long id) {
        var link = findById(id);
        if (link.isEmpty()) return false;
        entityManager.remove(link.get());
        return true;
    }

    @Override
    public List<Link> findAllByChat(Chat chat) {
        return chat.getLinks().stream().toList();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        var query = entityManager.createQuery(SELECT_BY_URL, Link.class);
        query.setParameter("url", url);
        return query.getResultStream().findAny();
    }

    @Override
    public List<Link> findLeastRecentlyUpdated(OffsetDateTime oldThanDateTime) {
        var query = entityManager.createQuery(SELECT_LEAST_RECENTLY_UPDATED, Link.class);
        query.setParameter("date", oldThanDateTime);
        return query.getResultList();
    }

    @Override
    public boolean removeFromChat(Chat chat, Link link) {
        return link.removeFromChat(chat);
    }

    @Override
    public boolean addToChat(Chat chat, Link link) {
        return link.addToChat(chat);
    }
}
