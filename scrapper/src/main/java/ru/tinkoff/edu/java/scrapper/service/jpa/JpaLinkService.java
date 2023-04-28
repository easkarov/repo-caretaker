package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Transactional
    @Override
    public Link track(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url).orElseGet(() -> linkRepository.save(new Link().setUrl(url)));
        Chat chat = chatRepository.findById(tgChatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        link.addToChat(chat);
        return link;
    }

    @Transactional
    @Override
    public Link untrack(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Link not found"));
        Chat chat = chatRepository.findById(tgChatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        link.removeFromChat(chat);
        return link;
    }

    @Transactional
    @Override
    public List<Link> listAll(long tgChatId) {
        var chat = chatRepository.findById(tgChatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        return chat.getLinks().stream().toList();
    }
}
