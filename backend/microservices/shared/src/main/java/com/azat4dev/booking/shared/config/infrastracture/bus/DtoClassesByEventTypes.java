package com.azat4dev.booking.shared.config.infrastracture.bus;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public final class DtoClassesByEventTypes {

    private final Map<String, Class<?>> dtoClassesByEventTypes;

    public Optional<Class<?>> get(String eventType) {
        return Optional.ofNullable(dtoClassesByEventTypes.get(eventType));
    }
}
