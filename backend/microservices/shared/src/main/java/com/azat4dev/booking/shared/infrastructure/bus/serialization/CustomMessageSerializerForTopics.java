package com.azat4dev.booking.shared.infrastructure.bus.serialization;

import java.util.List;

public record CustomMessageSerializerForTopics(List<String> topics, MessageSerializer serializer) {

}
