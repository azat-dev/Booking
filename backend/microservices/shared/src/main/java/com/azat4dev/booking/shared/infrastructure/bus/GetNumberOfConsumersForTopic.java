package com.azat4dev.booking.shared.infrastructure.bus;

@FunctionalInterface
public interface GetNumberOfConsumersForTopic {

    int run(String topic);
}
