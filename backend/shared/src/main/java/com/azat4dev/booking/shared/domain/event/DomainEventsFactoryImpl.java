package com.azat4dev.booking.shared.domain.event;

import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class DomainEventsFactoryImpl implements DomainEventsFactory {

    private final EventIdGenerator eventIdGenerator;
    private final TimeProvider timeProvider;

    @Override
    public DomainEvent<?> issue(DomainEventPayload payload) {
        return new DomainEvent(
            eventIdGenerator.generate(),
            timeProvider.currentTime(),
            payload
        );
    }
}
