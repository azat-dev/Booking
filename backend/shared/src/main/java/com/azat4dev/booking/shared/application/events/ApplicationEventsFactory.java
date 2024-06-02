package com.azat4dev.booking.shared.application.events;

public interface ApplicationEventsFactory {
    ApplicationEvent<?> issue(ApplicationEventPayload payload);
}
