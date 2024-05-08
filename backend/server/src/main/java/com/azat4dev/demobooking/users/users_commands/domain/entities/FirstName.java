package com.azat4dev.demobooking.users.users_commands.domain.entities;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;

import java.util.Objects;

public final class FirstName {

    public static final int MAX_LENGTH = 255;
    private final String value;

    public FirstName(String value) throws CantBeEmptyException, MaxLengthException {

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
        if (!(o instanceof FirstName firstName)) return false;
        return Objects.equals(getValue(), firstName.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    @Override
    public String toString() {
        return value;
    }

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class CantBeEmptyException extends ValidationException {
        public CantBeEmptyException() {
            super("The first name cannot be empty");
        }

        @Override
        public String getCode() {
            return "Empty";
        }
    }

    public static class MaxLengthException extends ValidationException {
        public MaxLengthException() {
            super("The maximum length of first name is " + MAX_LENGTH);
        }

        @Override
        public String getCode() {
            return "MaxLength";
        }
    }
}
