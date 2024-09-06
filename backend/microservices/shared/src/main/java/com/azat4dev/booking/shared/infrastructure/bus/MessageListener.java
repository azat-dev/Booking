package com.azat4dev.booking.shared.infrastructure.bus;

import java.util.Optional;
import java.util.Set;

@FunctionalInterface
public interface MessageListener {

    default Optional<Set<String>> messageTypes() {
        return Optional.empty();
    }

    void consume(Message message);
}
