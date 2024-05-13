package com.azat4dev.demobooking.common.domain.event;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class DomainEventsFactoryImpl implements DomainEventsFactory {

    private final EventIdGenerator eventIdGenerator;
    private final TimeProvider timeProvider;

    @Override
    public DomainEventNew<?> issue(DomainEventPayload payload) {
        return new DomainEventNew(
            eventIdGenerator.generate(),
            timeProvider.currentTime(),
            payload
        );
    }
}
