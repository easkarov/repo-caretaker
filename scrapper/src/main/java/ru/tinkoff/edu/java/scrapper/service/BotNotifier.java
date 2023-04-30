package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

public interface BotNotifier {
    void notify(LinkUpdate update);
}
