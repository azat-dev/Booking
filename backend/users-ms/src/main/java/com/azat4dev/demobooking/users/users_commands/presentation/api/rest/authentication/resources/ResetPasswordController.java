package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.ResetPasswordByEmail;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.CompletePasswordResetHandler;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.ResetPasswordByEmailHandler;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.CompleteResetPasswordRequest;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.ResetPasswordByEmailRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ResetPasswordController implements ResetPasswordResource {

    @Autowired
    ResetPasswordByEmailHandler handler;

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private CompletePasswordResetHandler completePasswordResetHandler;

    @Override
    public ResponseEntity<String> resetPasswordByEmail(
        @RequestBody ResetPasswordByEmailRequest requestBody,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var command = new ResetPasswordByEmail(
            requestBody.parseOperationId(),
            requestBody.parseEmail()
        );

        handler.handle(command, eventIdGenerator.generate(), timeProvider.currentTime());
        return ResponseEntity.ok("Password reset email sent");
    }

    @Override
    public ResponseEntity<String> completeResetPassword(
        CompleteResetPasswordRequest requestBody,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var command = requestBody.toCommand(passwordService);
        completePasswordResetHandler.handle(command, eventIdGenerator.generate(), timeProvider.currentTime());

        return ResponseEntity.ok("Password reset completed");
    }
}