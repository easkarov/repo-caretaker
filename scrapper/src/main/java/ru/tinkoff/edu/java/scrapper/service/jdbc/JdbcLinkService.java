package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.model.Link;
import ru.tinkoff.edu.java.scrapper.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.util.List;


@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        Link link = linkRepository.add(new Link().setUrl(url.toString()));
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        return null;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return null;
    }
}
