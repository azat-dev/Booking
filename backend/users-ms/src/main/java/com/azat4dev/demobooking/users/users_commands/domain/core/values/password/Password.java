package com.azat4dev.demobooking.users.users_commands.domain.core.values.password;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
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

    public static void validate(String password) throws WrongFormatException, LengthException {

        Assert.string(password, LengthException::new)
            .notNull()
            .notBlank()
            .maxLength(MAX_LENGTH)
            .minLength(MIN_LENGTH);

        Assert.string(password, WrongFormatException::new)
            .hasPattern(PATTERN);
    }

    public static Password makeFromString(String password) throws WrongFormatException, LengthException {
        validate(password);
        return new Password(password);
    }

    // Exceptions

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static final class LengthException extends ValidationException {
        public LengthException() {
            super("Length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);
        }

        @Override
        public String getCode() {
            return "WrongLength";
        }
    }

    public static final class WrongFormatException extends ValidationException {
        public WrongFormatException() {
            super("Wrong password format");
        }

        @Override
        public String getCode() {
            return "WrongFormat";
        }
    }
}
