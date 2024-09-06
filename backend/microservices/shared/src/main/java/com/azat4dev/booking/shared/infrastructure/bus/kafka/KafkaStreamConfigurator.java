package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.streams.kstream.KStream;

public interface KafkaStreamConfigurator {

    void configure(KStream<String, Message> stream);
}
