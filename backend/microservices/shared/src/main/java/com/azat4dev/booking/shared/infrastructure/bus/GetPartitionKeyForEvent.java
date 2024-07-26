package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

import java.util.Optional;

@FunctionalInterface
public interface GetPartitionKeyForEvent<K> {
    <T extends DomainEventPayload> Optional<K> execute(T event);
}