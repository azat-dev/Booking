package com.azat4dev.booking.searchlistingsms.helpers;

import org.awaitility.Awaitility;
import org.hamcrest.Matchers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class Helpers {
    public static void waitForValue(AtomicReference<?> receivedEventStore, Duration duration) {
        Awaitility.await()
            .pollInterval(Duration.ofMillis(1))
            .atMost(duration)
            .untilAtomic(receivedEventStore, Matchers.notNullValue());
    }
}
