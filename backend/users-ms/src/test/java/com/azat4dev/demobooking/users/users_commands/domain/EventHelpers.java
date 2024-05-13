package com.azat4dev.demobooking.users.users_commands.domain;

import com.azat4dev.demobooking.common.DomainEventsFactory;
import com.azat4dev.demobooking.common.DomainEventsFactoryImpl;
import com.azat4dev.demobooking.common.RandomEventIdGenerator;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;

public class EventHelpers {

    public static final DomainEventsFactory eventsFactory = new DomainEventsFactoryImpl(
        new RandomEventIdGenerator(),
        new SystemTimeProvider()
    );
}
