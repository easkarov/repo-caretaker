package ru.tinkoff.edu.java.scrapper;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
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
    ChatRepository chatRepository;

    @Test
    @Transactional
    public void addChat__ChatDoesntExistInDb_addedChat() {
        // given
        var chat = new Chat(444L);

        // when
        chatRepository.save(chat);

        // then
        Chat addedChat = jdbcTemplate.queryForObject("SELECT * FROM chat WHERE id = ?",
                new BeanPropertyRowMapper<>(Chat.class),
                chat.getId());

        assertThat(addedChat).isEqualTo(chat);
    }

    @Test
    @Transactional
    public void addChat__ChatAlreadyExistsInDb_throwException() {
        // given
        var chat = new Chat(444L);
        chatRepository.save(chat);

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.save(chat));
    }

    @Test
    @Transactional
    public void removeChat__ChatHasLinks_throwException() {
        // given
        var link = new Link().setId(123L).setUrl("http://stackoverflow.com/123123");
        var chat = new Chat(444L);

        chatRepository.save(chat);
        jdbcTemplate.update("INSERT INTO link(id, url) VALUES (?, ?)", link.getId(), link.getUrl());
        jdbcTemplate.update("INSERT INTO chat_link VALUES (?, ?)", chat.getId(), link.getId());

        // when, then
        assertThrows(DataIntegrityViolationException.class, () -> chatRepository.removeById(chat.getId()));
    }

    @Test
    @Transactional
    public void removeChat__ChatDoesntHaveLinks_oneRemovedLink() {
        // given
        var chat = new Chat(444L);
        chatRepository.save(chat);

        // when
        chatRepository.removeById(chat.getId());

        // then
        Boolean ifRemoved = jdbcTemplate.queryForObject("SELECT COUNT(*) = 0 FROM chat WHERE id = ?",
                Boolean.class, chat.getId());

        assertThat(ifRemoved).isTrue();
    }

    @Test
    @Transactional
    public void findAll__listOfChats() {
        // given
        var chatsToAdd = List.of(new Chat(444L), new Chat(111L));
        chatsToAdd.forEach(chatRepository::save);

        // when
        List<Chat> chats = chatRepository.findAll();

        // then
        assertThat(chats).hasSize(chats.size());

    }
}
