package com.azat4dev.booking.shared.domain.event;

public interface DomainEventsFactory {
    DomainEvent<?> issue(DomainEventPayload payload);
}
