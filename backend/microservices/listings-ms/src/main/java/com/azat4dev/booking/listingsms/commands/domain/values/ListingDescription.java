package com.azat4dev.booking.listingsms.commands.domain.values;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public final class ListingDescription {

    private final static int MAX_LENGTH = 10000;
    private final static int MIN_LENGTH = 1;
    private final String value;

    private ListingDescription(String value) {
        this.value = value;
    }

    public static ListingDescription checkAndMakeFrom(String value) throws Exception.Length {
        final var cleanedValue = value.trim();
        Assert.string(cleanedValue, () -> new Exception.Length(cleanedValue.length()))
            .minLength(MIN_LENGTH)
            .maxLength(MAX_LENGTH);

        return new ListingDescription(value);
    }

    public static ListingDescription dangerouslyMakeFrom(String value) {
        return new ListingDescription(value);
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class Length extends Exception {

            public Length(int length) {
                super("Description length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters, but got " + length);
            }
        }
    }
}
