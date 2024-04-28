package com.azat4dev.demobooking.users.domain.values;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public final class EmailAddress {

    private final String value;

    private EmailAddress(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    private static void validateEmail(String text) throws WrongEmailFormatException {

        final var validator = EmailValidator.getInstance();

        if (!validator.isValid(text)) {
            throw new WrongEmailFormatException(text);
        }
    }

    public static EmailAddress makeFromString(String text) throws WrongEmailFormatException {
        validateEmail(text);
        return new EmailAddress(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailAddress that)) return false;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
