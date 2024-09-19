package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Optional;

@AllArgsConstructor
public class DefaultStreamFactory implements StreamFactory {

    private final String topic;
    private final Serde<String> keySerde;
    private final Serde<Message> valueSerde;

    @Override
    public KStream<String, Message> make(StreamsBuilder builder) {
        return builder.stream(topic, Consumed.with(keySerde, valueSerde));
    }
}