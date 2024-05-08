package com.azat4dev.demobooking.users.users_commands.domain.values.email;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public final class EmailAddress {

    private final String value;

    private EmailAddress(String value) {
        this.value = value;
    }

    public static void validate(String value) throws WrongFormatException {

        final var validator = EmailValidator.getInstance();

        Assert.string(value, () -> new WrongFormatException(value))
            .notNull()
            .notBlank()
            .isTrue(validator::isValid);
    }

    public static EmailAddress checkAndMakeFromString(String text) throws WrongFormatException {
        final var cleanedText = text.trim().toLowerCase();
        validate(cleanedText);
        return new EmailAddress(cleanedText);
    }

    public static EmailAddress dangerMakeWithoutChecks(String text) {
        return new EmailAddress(text);
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
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

    public abstract static class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class WrongFormatException extends ValidationException {

        private final String email;

        public WrongFormatException(String email) {
            super("Wrong email format");
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String getCode() {
            return "WrongFormat";
        }
    }
}
