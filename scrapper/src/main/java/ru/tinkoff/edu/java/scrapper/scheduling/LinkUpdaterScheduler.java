package ru.tinkoff.edu.java.scrapper.scheduling;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@linkUpdateSchedulerIntervalMs}")
    public void update() {
        log.info("Updating...");
    }
}
