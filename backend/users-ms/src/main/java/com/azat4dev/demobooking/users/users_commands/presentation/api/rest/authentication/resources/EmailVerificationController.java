package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.common.presentation.ErrorDTO;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.CompleteEmailVerificationHandler;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@Validated
public class EmailVerificationController {

    @Autowired
    CompleteEmailVerificationHandler completeEmailVerificationHandler;

    @Autowired
    DomainEventsFactory domainEventsFactory;

    @ExceptionHandler({CompleteEmailVerificationHandler.ValidationException.class})
    ResponseEntity<ErrorDTO> handleTokenIsNotValidException(CompleteEmailVerificationHandler.ValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorDTO(ex.getCode(), ex.getMessage()));
    }

    @GetMapping("/verify-email")
    ResponseEntity<String> verifyEmail(
        @RequestParam("token") String rawToken,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        final var token = new EmailVerificationToken(rawToken);
        final var command = new CompleteEmailVerification(token);
        final var event = domainEventsFactory.issue(command);

        completeEmailVerificationHandler.handle((DomainEventNew<CompleteEmailVerification>) event);
        return ResponseEntity.ok("Email verified");
    }
}
