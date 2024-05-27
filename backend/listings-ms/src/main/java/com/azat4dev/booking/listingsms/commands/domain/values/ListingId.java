package com.azat4dev.booking.listingsms.commands.domain.values;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(of = "value")
@Getter
public final class ListingId {

    private final UUID value;

    private ListingId(UUID value) {
        this.value = value;
    }

    public static ListingId checkAndMakeFrom(String value) {
        return new ListingId(UUID.fromString(value));
    }

    public static ListingId dangerouslyMakeFrom(String value) {
        return new ListingId(UUID.fromString(value));
    }

    public String toString() {
        return value.toString();
    }
}
