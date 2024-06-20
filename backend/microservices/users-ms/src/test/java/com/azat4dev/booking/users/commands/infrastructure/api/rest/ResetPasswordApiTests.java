package com.azat4dev.booking.users.commands.infrastructure.api.rest;


import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.application.commands.password.CompletePasswordReset;
import com.azat4dev.booking.users.commands.application.commands.password.ResetPasswordByEmail;
import com.azat4dev.booking.users.commands.application.handlers.password.CompletePasswordResetHandler;
import com.azat4dev.booking.users.commands.application.handlers.password.ResetPasswordByEmailHandler;
import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;

import com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.ResetPasswordApi;
import com.azat4dev.booking.usersms.generated.server.model.CompleteResetPasswordRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.ResetPasswordByEmailRequestBodyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

public class ResetPasswordApiTests {

    SUT createSUT() {
        final var resetPasswordByEmailHandler = mock(ResetPasswordByEmailHandler.class);
        final var completePasswordResetHandler = mock(CompletePasswordResetHandler.class);

        return new SUT(
            new ResetPasswordApi(
                resetPasswordByEmailHandler,
                completePasswordResetHandler
            ),
            resetPasswordByEmailHandler,
            completePasswordResetHandler
        );
    }

    UUID anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return UUID.randomUUID();
    }

    @Test
    public void test_resetPasswordByEmail_givenEmail_thenPassToHandlerReturnOk() throws Exception {

        // Given
        final var sut = createSUT();

        final var request = new ResetPasswordByEmailRequestBodyDTO(
            anyIdempotentOperationId(),
            UserHelpers.anyValidEmail().getValue()
        );

        willDoNothing().given(sut.resetPasswordByEmailHandler)
            .handle(any());

        // When
        final var response = sut.api.resetPasswordByEmail(request);

        // Then
        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.OK);

        then(sut.resetPasswordByEmailHandler).should(times(1))
            .handle(
                new ResetPasswordByEmail(
                    request.getOperationId().toString(),
                    request.getEmail()
                )
            );
    }

    @Test
    public void test_resetPasswordByEmail_givenEmailNotFound_thenReturnBadRequest() throws Exception {

        // Given
        final var sut = createSUT();

        final var request = new ResetPasswordByEmailRequestBodyDTO(
            anyIdempotentOperationId(),
            "notValidEmail"
        );

        willThrow(new ResetPasswordByEmailHandler.Exception.EmailNotFound())
            .given(sut.resetPasswordByEmailHandler).handle(any());

        // When
        final var exception = assertThrows(
            ControllerException.class,
            () -> sut.api.resetPasswordByEmail(request)
        );

        // Then
        assertThat(exception.status())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void test_completeResetPassword_givenValidToken_thenPassToHandlerReturnOk() throws Exception {

        // Given
        final var sut = createSUT();

        final var operationId = anyIdempotentOperationId();
        final var tokenValue = "tokenValue";

        final var newPassword = "newPassword";
        final var token = TokenForPasswordReset.dangerouslyMakeFrom(tokenValue);

        final var request = new CompleteResetPasswordRequestBodyDTO(
            operationId,
            newPassword,
            tokenValue
        );

        // When
        final var response = sut.api.completeResetPassword(request);

        // Then
        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.OK);

        then(sut.completePasswordResetHandler).should(times(1))
            .handle(
                new CompletePasswordReset(
                    operationId.toString(),
                    newPassword,
                    token.getValue()
                )
            );
    }

    record SUT(
        ResetPasswordApi api,
        ResetPasswordByEmailHandler resetPasswordByEmailHandler,
        CompletePasswordResetHandler completePasswordResetHandler
    ) {
    }
}
