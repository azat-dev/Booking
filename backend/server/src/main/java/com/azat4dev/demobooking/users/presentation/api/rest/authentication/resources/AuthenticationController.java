package com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationController implements AuthenticationResource {

    @Override
    public ResponseEntity<SignUpResponse> signUp(
        @Valid SignUpRequest signUpRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var password1 = signUpRequest.password1();
        final var password2 = signUpRequest.password2();

        if (!password1.equals(password2)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .build();
        }

        return null;
    }
}
