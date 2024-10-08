package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

@FunctionalInterface
public interface GetInputChannelForEvent {
    String execute(Class<? extends DomainEventPayload> event);
}

