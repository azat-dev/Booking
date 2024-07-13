package com.azat4dev.booking.shared.domain.producers;

import java.io.Closeable;

public interface OutboxEventsReader extends Closeable {
    void trigger();
}
