package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.ResetPasswordByEmailHandler;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.ResetPasswordByEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Validated
public class ResetPasswordController {

    @Autowired
    ResetPasswordByEmailHandler handler;

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @Autowired
    private TimeProvider timeProvider;


    @PostMapping("/reset-password")
    ResponseEntity<String> resetPasswordByEmail(
        @RequestBody ResetPasswordByEmailRequest requestBody
    ) {

        throw new RuntimeException("Not implemented");
    }
}