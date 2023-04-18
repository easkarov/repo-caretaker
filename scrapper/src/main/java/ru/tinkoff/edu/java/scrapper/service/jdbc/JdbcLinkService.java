package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.DBException;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundException;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final JdbcLinkRepository linkRepository;

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
        Link link = linkRepository.findByUrl(url).orElseThrow(() -> new NotFoundException("Link not found"));
        linkRepository.removeFromChat(tgChatId, link.getId());
        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return linkRepository.findAllByChat(tgChatId);
    }
}
