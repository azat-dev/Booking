package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.common.presentation.ControllerException;
import com.azat4dev.booking.common.presentation.ErrorDTO;
import com.azat4dev.booking.shared.domain.event.EventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.application.handlers.password.ResetPasswordByEmailHandler;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.CompletePasswordResetHandler;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.PasswordService;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.CompleteResetPasswordRequest;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.ResetPasswordByEmailRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ResetPasswordController implements ResetPasswordResource {

    @Autowired
    ResetPasswordByEmailHandler resetPasswordByEmailHandler;

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private CompletePasswordResetHandler completePasswordResetHandler;

    @ExceptionHandler(ResetPasswordByEmailHandler.Exception.EmailNotFound.class)
    public ErrorDTO handleEmailNotFound(ResetPasswordByEmailHandler.Exception.EmailNotFound e) {
        return new ErrorDTO(e.getCode(), e.getMessage());
    }

    @Override
    public ResponseEntity<String> resetPasswordByEmail(
        @RequestBody ResetPasswordByEmailRequest requestBody,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        try {
            resetPasswordByEmailHandler.handle(requestBody);
        } catch (ResetPasswordByEmailHandler.Exception e) {
            throw ControllerException.createError(HttpStatus.BAD_REQUEST, e);
        }
        return ResponseEntity.ok("Password reset email sent");
    }

    @Override
    public ResponseEntity<String> completeResetPassword(
        CompleteResetPasswordRequest requestBody,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var command = requestBody.toCommand(passwordService);
        try {
            completePasswordResetHandler.handle(command, eventIdGenerator.generate(), timeProvider.currentTime());
        } catch (CompletePasswordResetHandler.Exception e) {
            throw ControllerException.createError(HttpStatus.BAD_REQUEST, e);
        }

        return ResponseEntity.ok("Password reset completed");
    }
}