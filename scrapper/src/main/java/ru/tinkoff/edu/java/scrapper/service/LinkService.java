package ru.tinkoff.edu.java.scrapper.service;


import ru.tinkoff.edu.java.scrapper.model.Link;

import java.net.URI;
import java.util.List;


public interface LinkService {
    Link add(long tgChatId, URI url);
    Link remove(long tgChatId, URI url);
    List<Link> listAll(long tgChatId);
}
