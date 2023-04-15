package ru.tinkoff.edu.java.scrapper.scheduling;


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
    public void update() {
        try {
            linkUpdater.update();
            log.info("All old links updated successfully");
        } catch (JsonProcessingException ignored) {
            log.info("Error occurred while updating");
        }
    }
}
