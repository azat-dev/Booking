package com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.users.commands.application.commands.password.CompletePasswordReset;
import com.azat4dev.booking.users.commands.application.commands.password.ResetPasswordByEmail;
import com.azat4dev.booking.users.commands.application.handlers.password.CompletePasswordResetHandler;
import com.azat4dev.booking.users.commands.application.handlers.password.ResetPasswordByEmailHandler;
import com.azat4dev.booking.usersms.generated.server.api.CommandsResetPasswordApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.CompleteResetPassword200ResponseDTO;
import com.azat4dev.booking.usersms.generated.server.model.CompleteResetPasswordRequestBodyDTO;
import com.azat4dev.booking.usersms.generated.server.model.ResetPasswordByEmail200ResponseDTO;
import com.azat4dev.booking.usersms.generated.server.model.ResetPasswordByEmailRequestBodyDTO;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Observed
@Component
@AllArgsConstructor
public class ResetPasswordApi implements CommandsResetPasswordApiDelegate {

    private final ResetPasswordByEmailHandler resetPasswordByEmailHandler;
    private final CompletePasswordResetHandler completePasswordResetHandler;

    @Override
    public ResponseEntity<ResetPasswordByEmail200ResponseDTO> resetPasswordByEmail(ResetPasswordByEmailRequestBodyDTO requestBody) {

        final var command = new ResetPasswordByEmail(
            requestBody.getOperationId().toString(),
            requestBody.getEmail()
        );

        try {
            resetPasswordByEmailHandler.handle(command);
        } catch (ResetPasswordByEmailHandler.Exception.EmailNotFound e) {
            throw ControllerException.createError(HttpStatus.NOT_FOUND, e);
        } catch (ResetPasswordByEmailHandler.Exception.FailedToSendResetPasswordEmail e) {
            throw ControllerException.createError(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        return ResponseEntity.ok(ResetPasswordByEmail200ResponseDTO.builder().build());
    }

    @Override
    public ResponseEntity<CompleteResetPassword200ResponseDTO> completeResetPassword(CompleteResetPasswordRequestBodyDTO requestBody) throws Exception {
        final var command = new CompletePasswordReset(
            requestBody.getOperationId().toString(),
            requestBody.getNewPassword(),
            requestBody.getResetPasswordToken()
        );

        try {
            completePasswordResetHandler.handle(command);
        } catch (CompletePasswordResetHandler.Exception e) {
            throw ControllerException.createError(HttpStatus.FORBIDDEN, e);
        }

        return ResponseEntity.ok(CompleteResetPassword200ResponseDTO.builder().build());
    }
}