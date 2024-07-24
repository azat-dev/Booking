package com.azat4dev.booking.shared.data.bus;

import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import jakarta.annotation.Nullable;

@FunctionalInterface
public interface GetClassForEventType {

    @Nullable
    Class<? extends DomainEventPayload> execute(String eventType);
}