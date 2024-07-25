package com.azat4dev.booking.listingsms.unit.helpers;

import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;

public class EventHelpers {

    public static EventId anyEventId() {
        return new RandomEventIdGenerator().generate();
    }

}
