package com.azat4dev.booking.users.commands.domain.values;


import com.azat4dev.booking.users.commands.domain.core.values.password.Password;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTests {

    public <T extends Password.Exception> void test_givenWrongPassword__thenThrowException(String wrongPassword, Class<T> exceptionClass) {

        // Given
        // wrongPassword

        // When
        final var exception = assertThrows(
            exceptionClass,
            () -> Password.checkAndMakeFromString(wrongPassword)
        );

        // Then
        assertThat(exception).isInstanceOf(exceptionClass);
    }

    @Test
    public void test_givenWrongPassword__thenThrowExceptionWithMessage() {

        test_givenWrongPassword__thenThrowException("123", Password.Exception.Length.class);
        test_givenWrongPassword__thenThrowException("123 5678", Password.Exception.WrongFormat.class);
    }

    @Test
    public void test_constructor_givenValidPassword_thenReturnObject() throws Password.Exception {

        // Given
        final var validValue = "validpassword";

        // When
        final var password = Password.checkAndMakeFromString(validValue);

        // Then
        assertThat(password).isNotNull();
    }
}
