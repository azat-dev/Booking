package com.azat4dev.demobooking.common;

public interface DomainEventsFactory {
    DomainEventNew<?> issue(DomainEventPayload payload);
}
