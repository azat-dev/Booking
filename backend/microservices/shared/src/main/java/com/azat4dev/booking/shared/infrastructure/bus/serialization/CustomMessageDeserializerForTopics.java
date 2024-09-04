package com.azat4dev.booking.shared.infrastructure.bus.serialization;

import java.util.List;

public record CustomMessageDeserializerForTopics(List<String> topics, MessageDeserializer serializer) {
}
