package com.azat4dev.booking.searchlistingsms.common.domain.values.address;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public final class Street {

    public static final int MAX_LENGTH = 255;

    private final String value;

    public static Street checkAndMakeFrom(String value) throws Exception.NotBlank, Exception.NotEmpty, Exception.TooLong {

        final var cleanedValue = value.trim();

        Assert.string(cleanedValue, Exception.NotBlank::new).notBlank();
        Assert.string(cleanedValue, Exception.NotEmpty::new).minLength(1);
        Assert.string(cleanedValue, () -> new Exception.TooLong(cleanedValue)).maxLength(MAX_LENGTH);

        return new Street(cleanedValue);
    }

    public static Street dangerouslyMakeFrom(String value) {
        return new Street(value);
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        protected Exception(String message) {
            super(message);
        }

        public static final class NotBlank extends Exception {
            public NotBlank() {
                super("Street must not be blank");
            }
        }

        public static final class TooLong extends Exception {
            public TooLong(String value) {
                super("Street must not be longer than " + MAX_LENGTH + " characters: " + value);
            }
        }

        public static final class NotEmpty extends Exception {
            public NotEmpty() {
                super("Street must not be empty");
            }
        }
    }
}
