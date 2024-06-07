package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.users.users_commands.application.commands.email.verification.CompleteEmailVerification;
import com.azat4dev.booking.users.users_commands.application.handlers.email.verification.CompleteEmailVerificationHandler;
import com.azat4dev.booking.usersms.generated.server.api.CommandsEmailVerificationApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.VerifyEmail200ResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailVerificationApi implements CommandsEmailVerificationApiDelegate {

    private final CompleteEmailVerificationHandler completeEmailVerificationHandler;

    @Override
    public ResponseEntity<VerifyEmail200ResponseDTO> verifyEmail(String token) throws Exception {

        final var command = new CompleteEmailVerification(token);
        try {
            completeEmailVerificationHandler.handle(command);
        } catch (CompleteEmailVerificationHandler.Exception e) {
            throw ControllerException.createError(HttpStatus.FORBIDDEN, e);
        }

        final var response = VerifyEmail200ResponseDTO.builder().build();
        return ResponseEntity.ok(response);
    }
}

