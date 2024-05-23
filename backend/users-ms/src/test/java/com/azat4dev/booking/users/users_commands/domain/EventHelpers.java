package com.azat4dev.booking.users.users_commands.domain;

import com.azat4dev.booking.shared.domain.event.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.event.DomainEventsFactoryImpl;
import com.azat4dev.booking.shared.domain.event.EventId;
import com.azat4dev.booking.shared.domain.event.RandomEventIdGenerator;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;

public class EventHelpers {

    public static EventId anyEventId() {
        return new RandomEventIdGenerator().generate();
    }

    public static final DomainEventsFactory eventsFactory = new DomainEventsFactoryImpl(
        new RandomEventIdGenerator(),
        new SystemTimeProvider()
    );
}
