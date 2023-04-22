package ru.tinkoff.edu.java.scrapper.jdbc;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.enums.ChatQuery;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import util.JdbcIntegrationEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@Rollback
public class JdbcChatRepositoryTest extends JdbcIntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcChatRepository chatRepository;

    @Test
    public void save__ChatDoesntExistInDb_addedChat() {
        // given
        var chat = new Chat().setId(444L);

        // when
        var savedChat = chatRepository.save(chat);

        assertThat(savedChat.getId()).isEqualTo(chat.getId());

        // then
        Chat realSavedChat = jdbcTemplate.queryForObject(ChatQuery.SELECT_BY_ID.query(),
                new BeanPropertyRowMapper<>(Chat.class), chat.getId());
        assertAll(
                () -> assertThat(realSavedChat).isNotNull(),
                () -> assertThat(realSavedChat).isEqualTo(savedChat)
        );
    }

    @Test
    @Sql("/sql/fill_in_chats.sql")
    public void save__ChatAlreadyExistsInDb_throwException() {
        // given
        var chat = new Chat().setId(1L);

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.save(chat));
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeById__chatHasLinks_throwException() {
        // given
        var chatId = 1;

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.removeById(chatId));
    }

    @Test
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeById__chatDoesntHaveLinks_removedChat() {
        // given
        var chatId = 4;

        // when
        Boolean ifRemoved = chatRepository.removeById(chatId);

        // then
        List<Chat> removedChat = jdbcTemplate.query(ChatQuery.SELECT_BY_ID.query(),
                new BeanPropertyRowMapper<>(Chat.class), chatId);
        assertAll(
                () -> assertThat(ifRemoved).isTrue(),
                () -> assertThat(removedChat).isEmpty()
        );
    }

    @Test
    @Sql("/sql/fill_in_chats.sql")
    public void findAll__dbHasSomeChats_notEmptyChatList() {
        // when
        List<Chat> chats = chatRepository.findAll();

        // then
        List<Chat> realChats = jdbcTemplate.query(ChatQuery.SELECT_ALL.query(),
                new BeanPropertyRowMapper<>(Chat.class));
        assertThat(chats).hasSameSizeAs(realChats);
    }

    @Test
    public void findAll__dbHasNoChats_emptyChatList() {
        // when
        List<Chat> chats = chatRepository.findAll();
        // then
        assertThat(chats).isEmpty();
    }

    @Sql("/sql/fill_in_chat_link_pairs.sql")
    @ParameterizedTest
    @ValueSource(longs = {2222, 2224, 2223})
    public void findAllByLink__chatHasSomeTrackedLinks_correctChatListSize(long linkId) {
        // when
        List<Chat> chats = chatRepository.findAllByLink(new Link().setId(linkId));
        // then
        List<Chat> realChats = jdbcTemplate.query(ChatQuery.SELECT_BY_LINK.query(),
                new BeanPropertyRowMapper<>(Chat.class), linkId);
        assertThat(chats).hasSameSizeAs(realChats);
    }
}
