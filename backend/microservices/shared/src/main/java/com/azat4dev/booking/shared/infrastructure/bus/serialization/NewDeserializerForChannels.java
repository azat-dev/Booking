package com.azat4dev.booking.shared.infrastructure.bus.serialization;

import java.util.List;

public record NewDeserializerForChannels(
    List<String> channels,
    MessageDeserializer deserializer
) {
}
