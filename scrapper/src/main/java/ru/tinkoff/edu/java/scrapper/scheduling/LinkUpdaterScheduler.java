package ru.tinkoff.edu.java.scrapper.scheduling;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.service.Updater;


@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final Updater linkUpdater;

    @Scheduled(fixedDelayString = "#{@linkUpdateSchedulerIntervalMs}")
    public void update() {
        linkUpdater.update();
    }
}
