package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.model.Chat;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.util.List;


@RequiredArgsConstructor
public class AbstractLinkService implements LinkService {

    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    @Transactional
    @Override
    public Link track(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url).orElseGet(() -> linkRepository.save(new Link().setUrl(url)));
        Chat chat = chatRepository.findById(tgChatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        linkRepository.addToChat(chat, link);
        return link;
    }

    @Transactional
    @Override
    public Link untrack(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Link not found"));
        Chat chat = chatRepository.findById(tgChatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        linkRepository.removeFromChat(chat, link);
        return link;
    }

    @Transactional
    @Override
    public List<Link> listAll(long tgChatId) {
        var chat = chatRepository.findById(tgChatId).orElseThrow(() -> new NotFoundException("Chat not found"));
        return linkRepository.findAllByChat(chat);
    }
}
