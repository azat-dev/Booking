package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.common.presentation.ErrorDTO;
import com.azat4dev.booking.users.users_commands.application.commands.email.verification.CompleteEmailVerification;
import com.azat4dev.booking.users.users_commands.application.handlers.CompleteEmailVerificationHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class EmailVerificationController implements EmailVerificationResource {

    @Autowired
    private CompleteEmailVerificationHandler completeEmailVerificationHandler;

    @ExceptionHandler({CompleteEmailVerificationHandler.Exception.class})
    public ResponseEntity<ErrorDTO> handleTokenIsNotValidException(CompleteEmailVerificationHandler.Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorDTO(ex.getCode(), ex.getMessage()));
    }

    @Override
    public ResponseEntity<String> verifyEmail(
        @RequestParam("token") String rawToken,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        final var command = new CompleteEmailVerification(rawToken);

        completeEmailVerificationHandler.handle(
            command
        );

        return ResponseEntity.ok("Email verified");
    }
}

