package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

@FunctionalInterface
public interface GetOutputChannelForEvent {
    String execute(DomainEventPayload event);
}