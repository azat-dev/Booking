package com.azat4dev.booking.shared.infrastructure.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

@FunctionalInterface
public interface GetInputTopicForEvent {
    String execute(Class<? extends DomainEventPayload> event);
}

