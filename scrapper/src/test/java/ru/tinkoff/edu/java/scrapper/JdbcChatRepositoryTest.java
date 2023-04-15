package ru.tinkoff.edu.java.scrapper;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import util.IntegrationEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
public class JdbcChatRepositoryTest extends IntegrationEnvironment {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcChatRepository chatRepository;

    @Test
    @Transactional
    public void save__ChatDoesntExistInDb_addedChat() {
        // given
        var chat = new Chat(444L);

        // when
        var savedChat = chatRepository.save(chat);

        assertThat(savedChat.getId()).isEqualTo(chat.getId());
        savedChat = chatRepository.findAll().get(0);
        assertThat(savedChat.getId()).isEqualTo(chat.getId());

        // then
        Chat addedChat = jdbcTemplate.queryForObject("SELECT * FROM chat WHERE id = ?",
                new BeanPropertyRowMapper<>(Chat.class),
                chat.getId());

        assertThat(addedChat).isEqualTo(chat);
    }

    @Test
    @Transactional
    public void save__ChatAlreadyExistsInDb_throwException() {
        // given
        var chat = new Chat(444L);
        chatRepository.save(chat);

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.save(chat));
    }

    @Test
    @Transactional
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeById__chatHasLinks_throwException() {
        // given
        var chatId = 1;

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.removeById(chatId));
    }

    @Test
    @Transactional
    @Sql("/sql/fill_in_chat_link_pairs.sql")
    public void removeById__chatDoesntHaveLinks_oneRemovedChat() {
        // given
        var chatId = 4;

        // when
        Boolean ifRemoved = chatRepository.removeById(chatId);
        assertThat(ifRemoved).isTrue();
        assertThat(chatRepository.findAll()).hasSize(3);

        // then
        ifRemoved = jdbcTemplate.queryForObject("SELECT COUNT(*) = 0 FROM chat WHERE id = ?",
                Boolean.class, chatId);
        assertThat(ifRemoved).isTrue();
    }

    @Test
    @Transactional
    @Sql("/sql/fill_in_chats.sql")
    public void findAll__listOfChats() {
        // when
        List<Chat> chats = chatRepository.findAll();

        // then
        assertThat(chats).hasSize(chats.size());

    }
}
