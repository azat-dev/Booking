package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.presentation.ErrorDTO;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification.CompleteEmailVerificationHandler;
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
    CompleteEmailVerificationHandler completeEmailVerificationHandler;

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @Autowired
    private TimeProvider timeProvider;

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

        final var token = new EmailVerificationToken(rawToken);
        final var command = new CompleteEmailVerification(token);

        completeEmailVerificationHandler.handle(
            command,
            eventIdGenerator.generate(),
            timeProvider.currentTime()
        );

        return ResponseEntity.ok("Email verified");
    }
}

