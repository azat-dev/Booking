package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.streams.kstream.KStream;

public interface KafkaStreamConnector {

    void connect(KStream<String, Message> stream);
}
