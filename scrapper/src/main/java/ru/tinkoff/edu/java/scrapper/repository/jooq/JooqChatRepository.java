package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;

import java.util.List;
import java.util.Optional;

import static ru.tinkoff.edu.java.scrapper.model.jooq.tables.Chat.CHAT;
import static ru.tinkoff.edu.java.scrapper.model.jooq.tables.ChatLink.CHAT_LINK;


@Primary
@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    @Override
    public List<Chat> findAll() {
        return dsl.select(CHAT.fields()).from(CHAT).fetchInto(Chat.class);
    }

    @Override
    public Optional<Chat> findById(long id) {
        return dsl.select(CHAT.fields()).from(CHAT).where(CHAT.ID.eq(id)).fetchOptionalInto(Chat.class);
    }

    @Override
    public List<Chat> findAllByLink(long linkId) {
        return dsl.select(CHAT.fields())
                .from(CHAT)
                .join(CHAT_LINK)
                .on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
                .where(CHAT_LINK.LINK_ID.eq(linkId))
                .fetchInto(Chat.class);
    }

    @Override
    public Chat save(Chat chat) {
        return dsl.insertInto(CHAT, CHAT.ID)
                .values(chat.getId())
                .returning(CHAT.fields())
                .fetchAnyInto(Chat.class);
    }

    @Override
    public boolean removeById(long id) {
        return dsl.deleteFrom(CHAT).where(CHAT.ID.eq(id)).execute() == 1;
    }

}
