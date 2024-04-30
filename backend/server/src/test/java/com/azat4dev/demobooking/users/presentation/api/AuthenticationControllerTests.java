package com.azat4dev.demobooking.users.presentation.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void singUp_givenNotMatchingPasswords_thenReturnError() throws Exception {

        // Given
        final var username = "username";
        final var password1 = "password";
        final var password2 = "password2";
        final var validEmail = "valid@email.com";

        final var request = new SignUpRequest(
            username,
            validEmail,
            password1,
            password2
        );

        // When
        final var response = performSignUpRequest(request);

        // Then
        response.andExpect(status().isBadRequest());
    }

    private ResultActions performPostRequest(
        String url,
        Object request
    ) throws Exception {

        final var payload = objectMapper.writeValueAsString(request);

        return mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        );
    }

    private ResultActions performSignUpRequest(SignUpRequest request) throws Exception {
        final String url = "/api/public/auth/sign-up";
        return performPostRequest(
                url,
                request
        );
    }

}
