package com.azat4dev.booking.shared.infrastructure.api.bus;

import com.azat4dev.booking.shared.infrastructure.bus.MessageBus;

import java.util.Optional;

public interface BusApiEndpoint<T> {

    void handle(InputMessage<T> message, MessageBus<String> messageBus);

    String getInputAddress();

    Optional<String> getReplyAddress();

    MessageInfo getInputMessageInfo();

    MessageInfo[] getResponseMessagesInfo();

    record MessageInfo(
        String messageType,
        Class<?> messageClass
    ) {
    }
}