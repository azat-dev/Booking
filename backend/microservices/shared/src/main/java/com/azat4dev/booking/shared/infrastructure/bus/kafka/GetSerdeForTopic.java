package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.common.serialization.Serde;

public interface GetSerdeForTopic {

    Serde<Message> run(String topic);
}
