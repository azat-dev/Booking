package com.azat4dev.booking.listingsms.commands.domain.values;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public final class ListingDescription {

    private final String value;

    private ListingDescription(String value) {
        this.value = value;
    }

    public static ListingDescription dangerouslyMakeFrom(String value) {
        return new ListingDescription(value);
    }
}
