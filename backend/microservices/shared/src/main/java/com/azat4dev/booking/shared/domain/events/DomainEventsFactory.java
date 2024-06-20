package com.azat4dev.booking.shared.domain.events;

public interface DomainEventsFactory {
    DomainEvent<?> issue(DomainEventPayload payload);
}
