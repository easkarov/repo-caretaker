package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.scrapper.model.jooq.public_.tables.ChatLink.CHAT_LINK;
import static ru.tinkoff.edu.java.scrapper.model.jooq.public_.tables.Link.LINK;


@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dsl;

    public List<Link> findAll() {
        return dsl.select(LINK.fields()).fetchInto(Link.class);
    }


    @Override
    public List<Link> findAllByChat(long chatId) {
        return dsl.select(LINK.fields())
                .from(LINK)
                .join(CHAT_LINK)
                .on(LINK.ID.eq(CHAT_LINK.LINK_ID)).where(CHAT_LINK.CHAT_ID.eq(chatId))
                .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return dsl.select(LINK.fields())
                .from(LINK)
                .where(LINK.URL.eq(url))
                .fetchOptionalInto(Link.class);
    }

    @Override
    public Optional<Link> findById(long id) {
        return dsl.select(LINK.fields())
                .from(LINK)
                .where(LINK.ID.eq(id))
                .fetchOptionalInto(Link.class);
    }

    @Override
    public List<Link> findLongUpdated(TemporalAmount delta) {
        return dsl.select(LINK.fields())
                .from(LINK)
                .where(LINK.UPDATED_AT.lessThan(OffsetDateTime.now().minus(delta)))
                .fetchInto(Link.class);
    }

    @Override
    public Link save(Link link) {
        if (link.getId() == null) {
            return dsl.insertInto(LINK, LINK.URL)
                    .values(link.getUrl())
                    .returning(LINK.fields())
                    .fetchAnyInto(Link.class);
        }

        return dsl.update(LINK)
                .set(LINK.URL, link.getUrl())
                .set(LINK.UPDATE_DATA, JSONB.valueOf(link.getUpdateData()))
                .set(LINK.UPDATED_AT, link.getUpdatedAt())
                .where(LINK.ID.eq(link.getId()))
                .returning(LINK.fields())
                .fetchOptionalInto(Link.class).orElseThrow();
    }

    @Override
    public boolean removeById(long id) {
        return dsl.deleteFrom(LINK).where(LINK.ID.eq(id)).execute() == 1;
    }

    @Override
    public boolean addToChat(long chatId, long linkId) {
        // TODO: add precondition: link doesn't exist in chat
        return dsl.insertInto(CHAT_LINK, CHAT_LINK.fields())
                .values(chatId, linkId)
                .execute() == 1;
    }

    @Override
    public boolean removeFromChat(long chatId, long linkId) {
        return dsl.deleteFrom(CHAT_LINK)
                .where(CHAT_LINK.CHAT_ID.eq(chatId), CHAT_LINK.LINK_ID.eq(linkId))
                .execute() == 1;
    }
}
