package ru.tinkoff.edu.java.scrapper.service.jooq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;


@Service
@Slf4j
@RequiredArgsConstructor
public class JooqChatService implements ChatService {

    private final JooqChatRepository chatRepository;

    @Transactional
    @Override
    public void register(long chatId) {
        if (chatRepository.findById(chatId).isEmpty()) {
            chatRepository.save(new Chat().setId(chatId));
            log.info("Chat with %s ID was registered".formatted(chatId));
        }
    }

    @Transactional
    @Override
    public void unregister(long chatId) {
        chatRepository.removeById(chatId);
    }
}
