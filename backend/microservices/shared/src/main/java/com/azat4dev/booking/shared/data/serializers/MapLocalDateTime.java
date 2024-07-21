package com.azat4dev.booking.shared.data.serializers;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public final class MapLocalDateTime implements Mapper<LocalDateTime, String> {

    private final DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String toDTO(LocalDateTime domain) {
        return domain.format(df);
    }

    @Override
    public LocalDateTime toDomain(String dto) {
        return LocalDateTime.parse(dto, df);
    }

    @Override
    public Class<LocalDateTime> getDomainClass() {
        return LocalDateTime.class;
    }

    @Override
    public Class<String> getDTOClass() {
        return String.class;
    }
}
