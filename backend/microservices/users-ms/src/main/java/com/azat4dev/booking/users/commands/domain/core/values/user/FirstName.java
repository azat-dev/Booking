package com.azat4dev.booking.users.commands.domain.core.values.user;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FirstName implements Serializable {

    public static final int MAX_LENGTH = 255;
    private final String value;

    @Override
    public String toString() {
        return value;
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

    public abstract static class ValidationException extends DomainException {
        protected ValidationException(String message) {
            super(message);
        }
    }

    public static final class CantBeEmptyException extends ValidationException {
        public CantBeEmptyException() {
            super("The first name cannot be empty");
        }

        @Override
        public String getCode() {
            return "Empty";
        }
    }

    public static final class MaxLengthException extends ValidationException {
        public MaxLengthException() {
            super("The maximum length of first name is " + MAX_LENGTH);
        }

        @Override
        public String getCode() {
            return "MaxLength";
        }
    }
}
