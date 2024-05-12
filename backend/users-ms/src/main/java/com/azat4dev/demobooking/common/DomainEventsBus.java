package com.azat4dev.demobooking.common;

import java.io.Closeable;
import java.util.function.Consumer;

public interface DomainEventsBus {

    void publish(DomainEventNew<?> event);

    Closeable listen(Class<DomainEventPayload> eventType, Consumer<DomainEventNew<?>> listener);
}
