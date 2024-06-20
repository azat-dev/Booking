package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsPublisher;
import com.azat4dev.booking.shared.domain.producers.OutboxEventsPublisherImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OutboxEventsPublisherConfig {

    @Bean
    public OutboxEventsPublisher outboxEventsPublisher(
        OutboxEventsRepository outboxEventsRepository,
        DomainEventsBus domainEventsBus
    ) {
        return new OutboxEventsPublisherImpl(outboxEventsRepository, domainEventsBus);
    }
}