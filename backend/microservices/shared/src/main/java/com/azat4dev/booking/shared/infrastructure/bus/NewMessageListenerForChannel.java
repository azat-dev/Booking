package com.azat4dev.booking.shared.infrastructure.bus;

import java.util.Optional;
import java.util.Set;

public record NewMessageListenerForChannel(
    String channel,
    Optional<Set<String>> messageTypes,
    MessageListener messageListener
) {

    public NewMessageListenerForChannel(
        String channel,
        MessageListener messageListener
    ) {
        this(
            channel,
            Optional.empty(),
            messageListener
        );
    }
}
