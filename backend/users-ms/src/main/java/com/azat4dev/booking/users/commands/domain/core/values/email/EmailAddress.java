package com.azat4dev.booking.users.commands.domain.core.values.email;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.*;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.Serializable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@Getter
@ToString(of = "value")
public final class EmailAddress implements Serializable {

    private final String value;

    public static void validate(String value) throws WrongFormatException {

        final var validator = EmailValidator.getInstance();

        Assert.string(value, () -> new WrongFormatException(value))
            .notNull()
            .notBlank()
            .isTrue(validator::isValid);
    }

    public static EmailAddress checkAndMakeFromString(String text) throws WrongFormatException {

        if (text == null) {
            throw new WrongFormatException("null");
        }

        final var cleanedText = text.trim().toLowerCase();
        validate(cleanedText);
        return new EmailAddress(cleanedText);
    }

    public static EmailAddress dangerMakeWithoutChecks(String text) {
        return new EmailAddress(text);
    }

    public abstract static class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    @Getter
    public static class WrongFormatException extends ValidationException {

        private final String email;

        public WrongFormatException(String email) {
            super("Wrong email format");
            this.email = email;
        }

        @Override
        public String getCode() {
            return "WrongFormat";
        }
    }
}
