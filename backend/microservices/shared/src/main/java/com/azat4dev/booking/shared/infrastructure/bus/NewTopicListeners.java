package com.azat4dev.booking.shared.infrastructure.bus;

import java.util.List;

public record NewTopicListeners(
    List<NewTopicMessageListener> items
) {
}
