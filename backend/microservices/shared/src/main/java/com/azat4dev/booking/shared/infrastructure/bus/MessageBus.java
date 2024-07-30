package com.azat4dev.booking.shared.infrastructure.bus;

import java.io.Closeable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public interface MessageBus<PARTITION_KEY> {

    <MESSAGE> void publish(
        String topic,
        Optional<PARTITION_KEY> partitionKey,
        String messageId,
        String messageType,
        MESSAGE message,
        Optional<String> replyTo,
        Optional<String> correlationId
    );

    <MESSAGE> void publish(
        String topic,
        Optional<PARTITION_KEY> partitionKey,
        String messageId,
        String messageType,
        MESSAGE message
    );

    Closeable listen(
        String topic,
        Consumer<ReceivedMessage> consumer
    );

    Closeable listen(
        String topic,
        Set<String> messageTypes,
        Consumer<ReceivedMessage> consumer
    );

    record ReceivedMessage(
        String messageId,
        String messageType,
        Optional<String> correlationId,
        Optional<String> replyTo,
        LocalDateTime messageSentAt,
        Object payload
    ) {

        public <T> T payload(Class<T> type) {
            return type.cast(payload);
        }
    }
}
