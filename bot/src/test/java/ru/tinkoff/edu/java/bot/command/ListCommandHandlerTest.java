package ru.tinkoff.edu.java.bot.command;


import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.MessageSender;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
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
    Update getUpdate(Long chatId) {
        Chat chat = new Chat();
        Field id = chat.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(chat, chatId);

        Message message = new Message();
        Field chatField = message.getClass().getDeclaredField("chat");
        chatField.setAccessible(true);
        chatField.set(message, chat);

        Update update = new Update();
        Field messageField = update.getClass().getDeclaredField("message");
        messageField.setAccessible(true);
        messageField.set(update, message);

        return update;
    }

    @Test
    void handle_ReturnMessageTellingNoLinks_EmptyLinkList() {
        // given

        final long CHAT_ID = new Random().nextLong();
        final Update UPDATE = getUpdate(CHAT_ID);
        when(linkService.getAllLinks(CHAT_ID)).thenReturn(List.of());

        // when
        SendMessage request = listCommandHandler.handle(UPDATE);

        // then
        assertThat(request.getParameters().get("text")).isEqualTo("There are no tracked links :(");
    }

    @Test
    void handle_ReturnMessageTellingSomeLinks_FilledLinkList() {
        // given
        final long CHAT_ID = new Random().nextLong();
        final URI FIRST_URL = URI.create("https://google.com");
        final URI SECOND_URL = URI.create("https://yandex.ru");

        List<LinkResponse> links = List.of(
                new LinkResponse(CHAT_ID, FIRST_URL),
                new LinkResponse(CHAT_ID, SECOND_URL)
        );
        Update update = getUpdate(CHAT_ID);

        when(linkService.getAllLinks(CHAT_ID)).thenReturn(links);

        // when
        SendMessage request = listCommandHandler.handle(update);

        // then
        assertThat(request.getParameters().get("text")).isEqualTo("%s\n%s".formatted(FIRST_URL, SECOND_URL));
    }
}
