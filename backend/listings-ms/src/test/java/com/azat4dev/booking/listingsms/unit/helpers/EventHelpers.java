package com.azat4dev.booking.listingsms.unit.helpers;

import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactoryImpl;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
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
