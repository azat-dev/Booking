package com.azat4dev.booking.shared.application.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class EventId {

    @Getter
    private final String value;

    private EventId(String value) {
        this.value = value;
    }

    public static EventId dangerouslyCreateFrom(String value) {
        return new EventId(value);
    }
}
