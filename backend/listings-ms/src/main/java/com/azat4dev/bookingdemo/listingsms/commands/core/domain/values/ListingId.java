package com.azat4dev.bookingdemo.listingsms.commands.core.domain.values;

import lombok.Getter;

import java.util.UUID;

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
