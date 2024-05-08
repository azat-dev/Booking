package com.azat4dev.demobooking.users.users_commands.domain.entities;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;

import java.util.Objects;

public final class LastName {

    public final static int MAX_LENGTH = 255;

    private final String value;

    public LastName(String value) throws CantBeEmptyException, MaxLengthException {

        final var cleanedValue = value.trim();
        Assert.string(cleanedValue, CantBeEmptyException::new).notNull().notBlank();
        Assert.string(cleanedValue, MaxLengthException::new).maxLength(MAX_LENGTH);

        this.value = cleanedValue;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LastName lastName)) return false;
        return Objects.equals(getValue(), lastName.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class CantBeEmptyException extends ValidationException {
        public CantBeEmptyException() {
            super("Last name cannot be empty");
        }

        @Override
        public String getCode() {
            return "Empty";
        }
    }

    public static class MaxLengthException extends ValidationException {
        public MaxLengthException() {
            super("Last name is too long");
        }

        @Override
        public String getCode() {
            return "MaxLength";
        }

    }
}
