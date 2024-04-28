package com.azat4dev.demobooking.users.domain.values;

import com.azat4dev.demobooking.users.domain.values.Email;
import com.azat4dev.demobooking.users.domain.values.WrongEmailFormatException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTests {

    @Test
    public void given_wrong_email__when_create_new__then_throw_exception() {

        // Given
        final var wrongEmail = "wrongemail.com";

        // When
        final var exception = assertThrows(
            WrongEmailFormatException.class,
            () -> Email.makeFromString(wrongEmail)
        );

        // Then
        assertThat(exception.getEmail()).isEqualTo(wrongEmail);
    }

    @Test
    public void given_valid_email__when_create_new__then_return_email() throws WrongEmailFormatException {

        // Given
        final var validEmail = "valid@email.com";

        // When
        final var email = Email.makeFromString(validEmail);

        // Then
        assertThat(email).isNotNull();
        assertThat(email.getValue()).isEqualTo(validEmail);
    }

    @Test
    public void given_equal_emails__when_isEqual__then_return_true() throws WrongEmailFormatException {

        // Given
        final var validEmail = "valid@email.com";
        final var email1 = Email.makeFromString(validEmail);
        final var email2 = Email.makeFromString(validEmail);

        // When
        final var isEqual = email1.equals(email2);


        // Then
        assertThat(isEqual).isTrue();
    }

    @Test
    public void given_equal_not_emails__when_isEqual__then_return_false() throws WrongEmailFormatException {

        // Given
        final var validEmail1 = "valid1@email.com";
        final var validEmail2 = "valid2@email.com";

        final var email1 = Email.makeFromString(validEmail1);
        final var email2 = Email.makeFromString(validEmail2);

        // When
        final var isEqual = email1.equals(email2);

        // Then
        assertThat(isEqual).isFalse();
    }
}
