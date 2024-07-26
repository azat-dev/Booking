package com.azat4dev.booking.shared.config.infrastracture.bus;

import java.util.Map;

@FunctionalInterface
public interface CustomizerForDtoClassesByMessageTypes {
    void customize(Map<String, Class<?>> dtoClassesByMessageTypes);
}
