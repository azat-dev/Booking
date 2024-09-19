package com.azat4dev.booking.shared.infrastructure.bus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Optional;

public interface MessageBus {

    <P> void publish(
        String channel,
        Optional<String> partitionKey,
        Data<P> message
    );


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Data<P> {
        private final String id;
        private final String type;
        private final Optional<String> correlationId;
        private final Optional<String> replyTo;
        private final P payload;

        public static <P> Data<P> with(String messageId, String messageType, P payload) {
            return new Data<>(messageId, messageType, Optional.empty(), Optional.empty(), payload);
        }

        public static <P> Data<P> with(String messageId, String messageType,
                                       Optional<String> correlationId, Optional<String> replyTo, P payload) {
            return new Data<>(messageId, messageType, correlationId, replyTo, payload);
        }

        public String id() {
            return id;
        }

        public String type() {
            return type;
        }

        public Optional<String> correlationId() {
            return correlationId;
        }

        public Optional<String> replyTo() {
            return replyTo;
        }

        public P payload() {
            return payload;
        }
    }
}