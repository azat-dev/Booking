package com.azat4dev.booking.listingsms.config.commands.infrastructure;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.tracing.ParseTracingInfo;
import com.azat4dev.booking.shared.data.tracing.PublishOutboxEventToBus;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExecuteWithTraceContext;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReader;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReaderImpl;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsReaderOneAtTime;
import com.azat4dev.booking.shared.domain.producers.PublishOutboxEvent;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@AllArgsConstructor
@Configuration
public class OutboxConfig {

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

        return new OutboxEventsReaderOneAtTime(reader);
    }

    @PreDestroy
    public void close() {

        final var reader = context.getBean(OutboxEventsReader.class);
        if (reader == null) {
            return;
        }

        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
