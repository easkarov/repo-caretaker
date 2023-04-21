package ru.tinkoff.edu.java.scrapper.schedule;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;


@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@linkUpdateSchedulerIntervalMs}")
    public void update() throws JsonProcessingException {
        linkUpdater.update();
        log.info("Old links updated successfully");
    }
}
