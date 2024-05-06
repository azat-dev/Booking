package com.azat4dev.demobooking.users.domain.values;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTests {

    @Test
    public void given_wrong_password__when_create_new__then_throw_exception() {

        // Given
        final var wrongPassword = "1234 ";

        // When
        final var exception = assertThrows(
            Password.WrongFormatException.class,
            () -> Password.makeFromString(wrongPassword)
        );

        // Then
        assertThat(exception).isInstanceOf(Password.WrongFormatException.class);
    }

    @Test
    public void given_valid_password__when_create_new__then_return_object() throws Password.WrongFormatException, Password.LengthException {

        // Given
        final var validValue = "validpassword";

        // When
        final var password = Password.makeFromString(validValue);

        // Then
        assertThat(password).isNotNull();
    }
}
