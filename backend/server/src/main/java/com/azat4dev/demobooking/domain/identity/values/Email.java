package com.azat4dev.demobooking.domain.identity.values;

import org.apache.commons.validator.routines.EmailValidator;

public class Email {

    private final String value;

    private Email(String value) {
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

    public static Email makeFromString(String text) throws WrongEmailFormatException {
        validateEmail(text);
        return new Email(text);
    }
}
