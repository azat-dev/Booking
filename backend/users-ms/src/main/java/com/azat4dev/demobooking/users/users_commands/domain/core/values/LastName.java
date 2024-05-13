package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.AccessLevel;


import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString(of = "value")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LastName implements Serializable {

    public final static int MAX_LENGTH = 255;

    private final String value;

    private static void validate(String value) throws LastName.CantBeEmptyException, LastName.MaxLengthException {
        Assert.string(value, LastName.CantBeEmptyException::new).notNull().notBlank();
        Assert.string(value, LastName.MaxLengthException::new).maxLength(MAX_LENGTH);
    }

    public static LastName checkAndMakeFromString(String value) throws LastName.CantBeEmptyException, LastName.MaxLengthException {

        Assert.notNull(value, LastName.CantBeEmptyException::new);
        final var cleanedValue = value.trim();

        validate(cleanedValue);
        return new LastName(cleanedValue);
    }

    public static LastName dangerMakeFromStringWithoutCheck(String value) {
        assert value != null;
        return new LastName(value);
    }

    // Exceptions

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static final class CantBeEmptyException extends ValidationException {
        public CantBeEmptyException() {
            super("Last name cannot be empty");
        }

        @Override
        public String getCode() {
            return "Empty";
        }
    }

    public static final class MaxLengthException extends ValidationException {
        public MaxLengthException() {
            super("Last name is too long");
        }

        @Override
        public String getCode() {
            return "MaxLength";
        }

    }
}