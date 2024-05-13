package com.azat4dev.demobooking.common.domain.event;

public interface DomainEventsFactory {
    DomainEventNew<?> issue(DomainEventPayload payload);
}
