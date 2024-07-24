package com.azat4dev.booking.shared.data.bus;

@FunctionalInterface
public interface GetClassForMessageType {

    Class<?> run(String messageType);
}