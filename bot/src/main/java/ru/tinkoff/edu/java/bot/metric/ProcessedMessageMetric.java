package ru.tinkoff.edu.java.bot.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProcessedMessageMetric {

    private static final String PROCESSED_MSG_COUNT_METRIC = "processed_tg_message_count";

    private final Counter processedMessageCounter;

    public ProcessedMessageMetric(MeterRegistry registry) {
        this.processedMessageCounter = registry.counter(PROCESSED_MSG_COUNT_METRIC);
    }

    public void incrementProcessedMessageCount() {
        processedMessageCounter.increment();
    }
}
