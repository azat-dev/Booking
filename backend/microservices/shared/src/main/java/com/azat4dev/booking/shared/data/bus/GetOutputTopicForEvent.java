package com.azat4dev.booking.shared.data.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;

@FunctionalInterface
public interface GetOutputTopicForEvent {
    String execute(DomainEventPayload event);
}