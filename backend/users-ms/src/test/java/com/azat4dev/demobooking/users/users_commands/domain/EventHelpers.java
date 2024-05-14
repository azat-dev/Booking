package com.azat4dev.demobooking.users.users_commands.domain;

import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactoryImpl;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.common.domain.event.RandomEventIdGenerator;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;

public class EventHelpers {

    public static EventId anyEventId() {
        return new RandomEventIdGenerator().generate();
    }

    public static final DomainEventsFactory eventsFactory = new DomainEventsFactoryImpl(
        new RandomEventIdGenerator(),
        new SystemTimeProvider()
    );
}
