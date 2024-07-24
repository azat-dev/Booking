package com.azat4dev.booking.shared.data.bus;

@FunctionalInterface
public interface GetNumberOfConsumersForTopic {

    int run(String topic);
}
