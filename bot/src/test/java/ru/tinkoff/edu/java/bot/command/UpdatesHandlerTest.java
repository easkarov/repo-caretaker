package ru.tinkoff.edu.java.bot.command;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.CommandsHandler;
import ru.tinkoff.edu.java.bot.MessageHandler;
import ru.tinkoff.edu.java.bot.UpdatesHandler;
import ru.tinkoff.edu.java.bot.dto.HandledUpdate;
import ru.tinkoff.edu.java.bot.enums.State;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UpdatesHandlerTest {

    UpdatesHandler updatesHandler;

    @Mock
    TelegramBot bot;

    @Mock
    CommandsHandler commandsHandler;

    @Mock
    MessageHandler messageHandler;

    @Captor
    ArgumentCaptor<SendMessage> captor;

    @BeforeEach
    void setup() {
        updatesHandler = new UpdatesHandler(bot, commandsHandler, messageHandler);
    }

    @SneakyThrows
    Update getUpdate(Long chatId, String text) {
        Chat chat = new Chat();
        Field id = chat.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(chat, chatId);

        Message message = new Message();
        Field chatField = message.getClass().getDeclaredField("chat");
        chatField.setAccessible(true);
        chatField.set(message, chat);
        Field textField = message.getClass().getDeclaredField("text");
        textField.setAccessible(true);
        textField.set(message, text);

        Update update = new Update();
        Field messageField = update.getClass().getDeclaredField("message");
        messageField.setAccessible(true);
        messageField.set(update, message);

        return update;
    }

    @Test
    void handle_executeUnknownCommandSendMessageRequest() {
        // given

        long chatId = new Random().nextLong();
        var helpMessage = "Unknown command. Try using /help.";
        var emptyHandledUpdate = HandledUpdate.EMPTY;
        var helpHandledUpdate = new HandledUpdate(Optional.of(new SendMessage(chatId, helpMessage)), State.NONE);

        Update update = getUpdate(chatId, "abracadabra");
        when(commandsHandler.handle(update)).thenReturn(emptyHandledUpdate);
        when(messageHandler.handle(update, State.NONE)).thenReturn(helpHandledUpdate);

        // when
        updatesHandler.process(List.of(update));
        verify(bot).execute(captor.capture());

        // then
        var requestMessage = captor.getValue().getParameters().get("text");
        assertThat(requestMessage).isEqualTo(helpMessage);
    }
}

