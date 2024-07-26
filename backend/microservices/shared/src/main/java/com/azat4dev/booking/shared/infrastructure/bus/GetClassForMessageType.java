package com.azat4dev.booking.shared.infrastructure.bus;

@FunctionalInterface
public interface GetClassForMessageType {

    Class<?> run(String messageType);
}