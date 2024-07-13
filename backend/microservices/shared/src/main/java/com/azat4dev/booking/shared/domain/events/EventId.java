package com.azat4dev.booking.shared.domain.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    @Override
    public String toString() {
        return value;
    }
}
