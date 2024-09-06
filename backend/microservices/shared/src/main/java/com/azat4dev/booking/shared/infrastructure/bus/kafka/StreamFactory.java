package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

@FunctionalInterface
public interface StreamFactory {
    KStream<String, Message> make(StreamsBuilder builder);
}
