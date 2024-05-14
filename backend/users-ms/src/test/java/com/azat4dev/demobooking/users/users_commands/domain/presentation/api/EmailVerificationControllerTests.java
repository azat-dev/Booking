package com.azat4dev.demobooking.users.users_commands.domain.presentation.api;

import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.users.users_commands.application.config.presentation.WebSecurityConfig;
import com.azat4dev.demobooking.users.users_commands.domain.EventHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.CompleteEmailVerificationHandler;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources.EmailVerificationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmailVerificationController.class)
@Import({WebSecurityConfig.class, TestConfig.class})
public class EmailVerificationControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CompleteEmailVerificationHandler completeEmailVerificationHandler;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void test_verifyEmail_givenInvalidToken_thenReturnError() throws Exception {

        // Given
        final var invalidToken = "invalidToken";

        willThrow(new CompleteEmailVerificationHandler.TokenIsNotValidException())
            .given(completeEmailVerificationHandler).handle(any());

        // When
        final var response = performVerifyEmailRequest(invalidToken);

        // Then
        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
    }

    @Test
    void test_verifyEmail_givenValidToken_thenReturnOk() throws Exception {

        // Given
        final var validToken = "validToken";

        // When
        final var response = performVerifyEmailRequest(validToken);

        // Then
        then(completeEmailVerificationHandler).should(times(1))
            .handle(assertArg(arg -> arg.payload().token().equals(validToken)));
        response.andExpect(status().isOk());
    }

    private ResultActions performVerifyEmailRequest(String token) throws Exception {
        final String url = "/api/public/verify-email";
        return mockMvc.perform(get(url).param("token", token))
            .andDo(MockMvcResultHandlers.print());
    }
}

@Configuration
class TestConfig {
    @Bean
    DomainEventsFactory domainEventsFactory() {
        return EventHelpers.eventsFactory;
    }
}