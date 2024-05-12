package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public final class FirstName implements Serializable {

    public static final int MAX_LENGTH = 255;
    private final String value;

    private FirstName(String value) {
        this.value = value;
    }

    private static void validate(String value) throws CantBeEmptyException, MaxLengthException {
        Assert.string(value, CantBeEmptyException::new).notNull().notBlank();
        Assert.string(value, MaxLengthException::new).maxLength(MAX_LENGTH);
    }

    public static FirstName checkAndMakeFromString(String value) throws CantBeEmptyException, MaxLengthException {

        Assert.notNull(value, CantBeEmptyException::new);
        final var cleanedValue = value.trim();

        validate(cleanedValue);
        return new FirstName(cleanedValue);
    }

    public static FirstName dangerMakeFromStringWithoutCheck(String value) {

        assert value != null;
        return new FirstName(value);
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
