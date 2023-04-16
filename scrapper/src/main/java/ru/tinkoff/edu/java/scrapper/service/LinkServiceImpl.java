package ru.tinkoff.edu.java.scrapper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.DBException;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

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
        Link link = linkRepository.findByUrl(url).orElseThrow(() -> new DBException("Link not found to untrack"));
        linkRepository.removeFromChat(tgChatId, link.getId());
        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return linkRepository.findAllByChat(tgChatId);
    }
}
