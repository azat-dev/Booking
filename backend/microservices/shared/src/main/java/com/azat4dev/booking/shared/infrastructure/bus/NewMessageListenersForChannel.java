package com.azat4dev.booking.shared.infrastructure.bus;

import java.util.List;

public record NewMessageListenersForChannel(
    List<NewMessageListenerForChannel> items
) {
}
