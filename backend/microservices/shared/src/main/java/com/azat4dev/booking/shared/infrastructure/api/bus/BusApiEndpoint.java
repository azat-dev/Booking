package com.azat4dev.booking.shared.infrastructure.api.bus;

import java.util.Optional;

public interface BusApiEndpoint<T> {

    void handle(InputMessage<T> message);

    String inputAddress();

    Optional<String> replyAddress();

    Class<T> getInputMessageType();
}