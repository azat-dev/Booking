package com.azat4dev.bookingdemo.listingsms.commands.core.domain.values;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.Getter;

@Getter
public final class ListingTitle {

    public static final int MAX_LENGTH = 255;

    private final String value;

    private ListingTitle(String value) {
        this.value = value;
    }

    public static ListingTitle checkAndMakeFrom(String value) throws Exception.WrongFormat, Exception.MaxLength {
        final var cleanedValue = value.trim();

        Assert.notNull(cleanedValue, () -> new Exception.WrongFormat(cleanedValue));
        Assert.notBlank(cleanedValue, () -> new Exception.WrongFormat(cleanedValue));

        Assert.string(value, () -> new Exception.MaxLength(value)).maxLength(MAX_LENGTH);
        return new ListingTitle(cleanedValue);
    }

    public static ListingTitle dangerouslyMakeFrom(String value) {
        return new ListingTitle(value);
    }

    public String toString() {
        return value;
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static class WrongFormat extends DomainException {
            public WrongFormat(String value) {
                super("Wrong formatted listing title. Value: " + value);
            }
        }

        public static class MaxLength extends DomainException {
            public MaxLength(String value) {
                super("Listing title is too long. Value: " + value);
            }
        }
    }
}