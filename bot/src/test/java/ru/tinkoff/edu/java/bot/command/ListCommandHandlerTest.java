package ru.tinkoff.edu.java.bot.command;


import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.MessageHandler;
import ru.tinkoff.edu.java.bot.MessageSender;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ListCommandHandlerTest {

    @Mock
    LinkService linkService;

    ListCommandHandler listCommandHandler;

    @BeforeEach
    void setup() {
        listCommandHandler = new ListCommandHandler(new MessageSender(), linkService);
    }

    @SneakyThrows
    static Update getUpdate(Long chatId) {
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();

        Field id = chat.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(chat, chatId);

        Field chatField = message.getClass().getDeclaredField("chat");
        chatField.setAccessible(true);
        chatField.set(message, chat);

        Field messageField = update.getClass().getDeclaredField("message");
        messageField.setAccessible(true);
        messageField.set(update, message);

        return update;
    }

    @Test
    void handle_returnMessageTellingNoLinks() {
        // given

        long chatId = 123;
        Update update = getUpdate(chatId);
        when(linkService.getAllLinks(chatId)).thenReturn(List.of());

        // when
        SendMessage request = listCommandHandler.handle(update);

        // then
        assertThat(request.getParameters().get("text")).isEqualTo("There are no tracked links :(");
    }

    @Test
    void handle_returnMessageTellingSomeLinks() {
        // given
        long chatId = 123L;
        URI firstUrl = URI.create("https://google.com");
        URI secondUrl = URI.create("https://yandex.ru");

        List<LinkResponse> links = List.of(new LinkResponse(chatId, firstUrl),
                new LinkResponse(chatId, secondUrl));
        Update update = getUpdate(chatId);

        when(linkService.getAllLinks(chatId)).thenReturn(links);

        // when
        SendMessage request = listCommandHandler.handle(update);

        // then
        assertThat(request.getParameters().get("text")).isEqualTo("%s\n%s".formatted(firstUrl, secondUrl));
    }
}
