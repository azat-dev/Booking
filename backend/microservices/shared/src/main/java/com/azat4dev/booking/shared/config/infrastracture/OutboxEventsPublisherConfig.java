package com.azat4dev.booking.shared.config.infrastracture;

import com.azat4dev.booking.shared.infrastructure.persistence.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.infrastructure.tracing.ParseTracingInfo;
import com.azat4dev.booking.shared.infrastructure.tracing.PublishOutboxEventToBus;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExecuteWithTraceContext;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReader;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReaderImpl;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReaderOneAtTime;
import com.azat4dev.booking.shared.domain.producers.PublishOutboxEvent;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Configuration
public class OutboxEventsPublisherConfig {

    private final ApplicationContext context;
    private final DomainEventsBus bus;
    private final ExecuteWithTraceContext executeWithTrace;
    private final ParseTracingInfo parseTracingInfo;
    private final OutboxEventsRepository outboxEventsRepository;


    @Bean
    public PublishOutboxEvent publishOutboxEvent() {
        return new PublishOutboxEventToBus(
            parseTracingInfo,
            bus,
            executeWithTrace
        );
    }

    @Bean
    public OutboxEventsReader outboxEventsReader(PublishOutboxEvent publishOutboxEvent) {
        final var reader = new OutboxEventsReaderImpl(
            outboxEventsRepository,
            publishOutboxEvent
        );

        final var readerOneAtTime = new OutboxEventsReaderOneAtTime(reader);
        readerOneAtTime.trigger();
        return readerOneAtTime;
    }

    @PreDestroy
    public void close() {

        final var reader = context.getBean(OutboxEventsReader.class);
        if (reader == null) {
            log.atError()
                .log("Outbox reader not found");
            return;
        }

        try {
            reader.close();
        } catch (IOException e) {
            log.atError()
                .setCause(e)
                .log("Error closing outbox reader");
            throw new RuntimeException(e);
        }
    }
}
