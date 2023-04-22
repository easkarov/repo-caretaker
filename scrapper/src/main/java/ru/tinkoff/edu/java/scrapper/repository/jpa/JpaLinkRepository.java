package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.model.Link;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    List<Link> findAllByUpdatedAtBefore(OffsetDateTime dateTime);
}
