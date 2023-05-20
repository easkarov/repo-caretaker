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
import org.springframework.test.util.ReflectionTestUtils;
import ru.tinkoff.edu.java.bot.MessageSender;
import ru.tinkoff.edu.java.bot.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.LinkService;

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
        ReflectionTestUtils.setField(chat, "id", chatId);

        Message message = new Message();
        ReflectionTestUtils.setField(message, "chat", chat);

        Update update = new Update();
        ReflectionTestUtils.setField(update, "message", message);

        return update;
    }

    @Test
    void handle_ReturnMessageTellingNoLinks_EmptyLinkList() {
        // given

        var chatId = new Random().nextLong();
        var update = getUpdate(chatId);
        when(linkService.getAllLinks(chatId)).thenReturn(List.of());

        // when
        SendMessage request = listCommandHandler.handle(update);

        // then
        assertThat(request.getParameters().get("text")).isEqualTo("There are no tracked links");
    }

    @Test
    void handle_ReturnMessageTellingSomeLinks_FilledLinkList() {
        // given
        var chatId = new Random().nextLong();
        var firstUrl = URI.create("https://google.com");
        var secondUrl = URI.create("https://yandex.ru");

        List<LinkResponse> links = List.of(
                new LinkResponse(chatId, firstUrl),
                new LinkResponse(chatId, secondUrl)
        );
        var update = getUpdate(chatId);

        when(linkService.getAllLinks(chatId)).thenReturn(links);

        // when
        SendMessage request = listCommandHandler.handle(update);

        // then
        assertThat(request.getParameters().get("text")).isEqualTo("Your links:\n%s\n%s".formatted(firstUrl, secondUrl));
    }
}
