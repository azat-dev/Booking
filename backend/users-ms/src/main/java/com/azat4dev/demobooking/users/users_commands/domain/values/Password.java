package com.azat4dev.demobooking.users.users_commands.domain.values;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;

public final class Password {

    public final static int MIN_LENGTH = 6;
    public final static int MAX_LENGTH = 255;
    public final static String PATTERN = "\\S+";
    private final String value;

    private Password(String value) {
        this.value = value;
    }

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

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Password other)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return this.value.equals(other.getValue());
    }

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class LengthException extends ValidationException {
        public LengthException() {
            super("Length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);
        }

        @Override
        public String getCode() {
            return "WrongLength";
        }
    }

    public static class WrongFormatException extends ValidationException {
        public WrongFormatException() {
            super("Wrong password format");
        }

        @Override
        public String getCode() {
            return "WrongFormat";
        }
    }
}
