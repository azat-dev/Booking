package com.azat4dev.booking.users.commands.domain.core.values.password;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
public final class Password {

    public final static int MIN_LENGTH = 6;
    public final static int MAX_LENGTH = 255;
    public final static String PATTERN = "\\S+";

    @Getter
    private final String value;

    public static void validate(String password) throws Exception {

        Assert.notNull(password, Exception.WrongFormat::new);
        Assert.string(password, Exception.Length::new)
            .notNull()
            .notBlank()
            .maxLength(MAX_LENGTH)
            .minLength(MIN_LENGTH);

        Assert.string(password, Exception.WrongFormat::new)
            .hasPattern(PATTERN);
    }

    public static Password checkAndMakeFromString(String password) throws Exception {
        validate(password);
        return new Password(password);
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class Length extends Exception {
            public Length() {
                super("Length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);
            }
        }

        public static final class WrongFormat extends Exception {
            public WrongFormat() {
                super("Wrong password format");
            }
        }

    }
}
