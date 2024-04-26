package com.azat4dev.demobooking.domain.identity.values;

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
    public void given_valid_email__when_create_new__then_return_email() {

        // Given
        final var validEmail = "valid@email.com";

        // When
        final var email = Email.makeFromString(validEmail);

        // Then
        assertThat(email).isNotNull();
        assertThat(email.getValue()).isEqualTo(validEmail);
    }
}
