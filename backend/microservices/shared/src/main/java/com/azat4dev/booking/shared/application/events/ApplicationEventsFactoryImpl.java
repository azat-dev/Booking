package com.azat4dev.booking.shared.application.events;

import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ApplicationEventsFactoryImpl implements ApplicationEventsFactory {

    private final EventIdGenerator eventIdGenerator;
    private final TimeProvider timeProvider;

    @Override
    public ApplicationEvent<?> issue(ApplicationEventPayload payload) {
        return new ApplicationEvent(
            eventIdGenerator.generate(),
            timeProvider.currentTime(),
            payload
        );
    }
}
