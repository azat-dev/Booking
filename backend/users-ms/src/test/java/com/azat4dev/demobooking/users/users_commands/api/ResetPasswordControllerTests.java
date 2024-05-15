package com.azat4dev.demobooking.users.users_commands.api;


import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.domain.event.RandomEventIdGenerator;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.users_commands.application.config.presentation.WebSecurityConfig;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.ResetPasswordByEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.ResetPasswordByEmailHandler;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.ResetPasswordByEmailRequest;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources.ResetPasswordController;
import com.azat4dev.demobooking.users.users_queries.domain.services.UsersQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResetPasswordController.class)
@Import(WebSecurityConfig.class)
public class ResetPasswordControllerTests {

    @Autowired
    ApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UsersQueryService usersService;
    @MockBean
    private JwtService tokenProvider;
    @MockBean
    private JwtEncoder jwtEncoder;
    @MockBean
    @Qualifier("accessTokenDecoder")
    private JwtDecoder accessTokenDecoder;

    @MockBean
    private ResetPasswordByEmailHandler resetPasswordByEmailHandler;

    @Test
    public void test_resetPasswordByEmail_givenValidToken_thenPassToHandlerReturnOk() throws Exception {

        // Given
        final var request = new ResetPasswordByEmailRequest(
            "idempotentOperationKey",
            UserHelpers.anyValidEmail().getValue()
        );

        willDoNothing().given(resetPasswordByEmailHandler)
            .handle(any(), any(), any());

        // When
        final var result = performResetPasswordByEmail(request);

        // Then
        result.andExpect(status().isOk());

        final var expectedCommand = new ResetPasswordByEmail(
            request.idempotentOperationId(),
            EmailAddress.dangerMakeWithoutChecks(request.email())
        );

        then(resetPasswordByEmailHandler).should(times(1))
            .handle(
                eq(expectedCommand),
                any(),
                any()
            );
    }


    private ResultActions performResetPasswordByEmail(ResetPasswordByEmailRequest request) throws Exception {
        final String url = "/api/public/reset-password";
        return performPostRequest(
            url,
            request
        );
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

    @TestConfiguration
    static class Config {
        @Bean
        EventIdGenerator eventIdGenerator() {
            return new RandomEventIdGenerator();
        }

        @Bean
        TimeProvider timeProvider() {
            return new SystemTimeProvider();
        }
    }
}
