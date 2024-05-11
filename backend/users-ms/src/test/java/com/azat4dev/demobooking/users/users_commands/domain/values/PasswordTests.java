package com.azat4dev.demobooking.users.users_commands.domain.values;


import com.azat4dev.demobooking.users.users_commands.domain.values.Password;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTests {

    public <T extends Password.ValidationException> void test_givenWrongPassword__thenThrowException(String wrongPassword, Class<T> exceptionClass) {

        // Given
        // wrongPassword

        // When
        final var exception = assertThrows(
            exceptionClass,
            () -> Password.makeFromString(wrongPassword)
        );

        // Then
        assertThat(exception).isInstanceOf(exceptionClass);
    }

    @Test
    public void test_givenWrongPassword__thenThrowExceptionWithMessage() {

        test_givenWrongPassword__thenThrowException("123", Password.LengthException.class);
        test_givenWrongPassword__thenThrowException("123 5678", Password.WrongFormatException.class);
    }

    @Test
    public void test_constructor_givenValidPassword_thenReturnObject() throws Password.WrongFormatException, Password.LengthException {

        // Given
        final var validValue = "validpassword";

        // When
        final var password = Password.makeFromString(validValue);

        // Then
        assertThat(password).isNotNull();
    }
}
