package com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationController implements AuthenticationResource {

    @Override
    public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
