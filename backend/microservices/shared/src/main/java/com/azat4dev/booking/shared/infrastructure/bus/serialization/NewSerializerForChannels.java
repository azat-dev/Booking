package com.azat4dev.booking.shared.infrastructure.bus.serialization;

import java.util.List;

public record NewSerializerForChannels(
    List<String> channels,
    MessageSerializer serializer
) {

}
