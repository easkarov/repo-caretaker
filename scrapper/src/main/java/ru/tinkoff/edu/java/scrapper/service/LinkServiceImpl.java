package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final JooqLinkRepository linkRepository;

    @Transactional
    @Override
    public Link track(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url).orElseGet(() -> linkRepository.save(new Link().setUrl(url)));
        linkRepository.addToChat(tgChatId, link.getId());
        return link;
    }

    @Transactional
    @Override
    public Link untrack(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url).orElseThrow(() -> new RuntimeException("link not found"));
        linkRepository.removeFromChat(tgChatId, link.getId());
        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return linkRepository.findAllByChat(tgChatId);
    }
}
