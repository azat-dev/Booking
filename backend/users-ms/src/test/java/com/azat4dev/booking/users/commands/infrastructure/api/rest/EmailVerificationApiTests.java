package com.azat4dev.booking.users.commands.infrastructure.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.users.commands.application.handlers.email.verification.CompleteEmailVerificationHandler;
import com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.EmailVerificationApi;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

public class EmailVerificationApiTests {

    private SUT createSUT() {

        final var completeEmailVerificationHandler = mock(CompleteEmailVerificationHandler.class);

        return new SUT(
            new EmailVerificationApi(completeEmailVerificationHandler),
            completeEmailVerificationHandler
        );
    }

    @Test
    void test_verifyEmail_givenInvalidToken_thenReturnError() throws Exception {

        // Given
        final var sut = createSUT();
        final var invalidToken = "invalidToken";

        willThrow(new CompleteEmailVerificationHandler.Exception.TokenIsNotValid())
            .given(sut.handler)
            .handle(any());

        // When
        final var exception = assertThrows(
            ControllerException.class,
            () -> sut.api.verifyEmail(invalidToken)
        );

        // Then
        assertThat(exception.status())
            .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void test_verifyEmail_givenValidToken_thenReturnOk() throws Exception {

        // Given
        final var sut = createSUT();
        final var validToken = "validToken";

        // When
        final var response = sut.api.verifyEmail(validToken);

        // Then
        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.OK);
    }

    private record SUT(
        EmailVerificationApi api,
        CompleteEmailVerificationHandler handler
    ) {
    }
}
