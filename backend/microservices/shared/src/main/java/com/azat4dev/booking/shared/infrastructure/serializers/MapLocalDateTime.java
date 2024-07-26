package com.azat4dev.booking.shared.infrastructure.serializers;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public final class MapLocalDateTime implements Serializer<LocalDateTime, String> {

    private final DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String serialize(LocalDateTime domain) {
        return domain.format(df);
    }

    @Override
    public LocalDateTime deserialize(String dto) {
        return LocalDateTime.parse(dto, df);
    }

    @Override
    public Class<LocalDateTime> getOriginalClass() {
        return LocalDateTime.class;
    }

    @Override
    public Class<String> getSerializedClass() {
        return String.class;
    }
}
