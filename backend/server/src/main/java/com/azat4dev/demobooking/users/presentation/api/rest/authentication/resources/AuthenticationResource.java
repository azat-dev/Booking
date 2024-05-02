package com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.LoginByEmailRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.LoginByEmailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpResponse;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;

@RestController
@RequestMapping("/api/public/auth")
@Validated
public interface AuthenticationResource {

    @PostMapping("/sign-up")
    ResponseEntity<SignUpResponse> signUp(
            @Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception;

    @PostMapping("/token")
    ResponseEntity<LoginByEmailResponse> authenticate(
        @Valid @RequestBody LoginByEmailRequest authenticationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    );
}
