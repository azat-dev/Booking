package com.azat4dev.booking.shared.infrastructure.bus;

import java.util.Optional;
import java.util.Set;

public record NewTopicListener(
    String topic,
    Optional<Set<String>> messageTypes,
    MessageListener messageListener
) {

    public NewTopicListener(
        String topic,
        MessageListener messageListener
    ) {
        this(
            topic,
            Optional.empty(),
            messageListener
        );
    }
}
