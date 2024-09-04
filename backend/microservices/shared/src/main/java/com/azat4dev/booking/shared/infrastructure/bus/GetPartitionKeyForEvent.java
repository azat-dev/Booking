package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.util.Optional;

@FunctionalInterface
public interface GetPartitionKeyForEvent {
    <T extends DomainEventPayload> Optional<String> execute(T event);
}