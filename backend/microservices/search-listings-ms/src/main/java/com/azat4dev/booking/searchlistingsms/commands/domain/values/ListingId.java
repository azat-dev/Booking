package com.azat4dev.booking.searchlistingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.DomainException;
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

    public static ListingId checkAndMakeFrom(String value) throws Exception.WrongFormat {
        try {
            return new ListingId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new Exception.WrongFormat();
        }
    }

    public static ListingId dangerouslyMakeFrom(String value) {
        return new ListingId(UUID.fromString(value));
    }

    public String toString() {
        return value.toString();
    }

    // Exceptions

    public static class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static class WrongFormat extends Exception {
            public WrongFormat() {
                super("Listing ID has wrong format");
            }
        }
    }
}
