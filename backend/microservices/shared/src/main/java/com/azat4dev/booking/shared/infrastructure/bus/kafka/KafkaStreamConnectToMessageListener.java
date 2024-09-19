package com.azat4dev.booking.shared.infrastructure.bus.kafka;

import com.azat4dev.booking.shared.infrastructure.bus.Message;
import com.azat4dev.booking.shared.infrastructure.bus.MessageListener;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Set;

@AllArgsConstructor
public class KafkaStreamConnectToMessageListener implements KafkaStreamConnector {

    private final MessageListener messageListener;

    @Override
    public void connect(KStream<String, Message> stream) {

        final var messageTypes = messageListener.messageTypes()
            .orElse(Set.of());

        if (!messageTypes.isEmpty()) {
            stream = stream.filter(
                (key, message) -> messageTypes.contains(message.type())
            );
        }

        stream.foreach(
            (key, message) -> messageListener.consume(message)
        );
    }
}
