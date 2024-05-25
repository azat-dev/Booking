package com.azat4dev.booking.users.users_commands.api;


import com.azat4dev.booking.shared.domain.event.EventIdGenerator;
import com.azat4dev.booking.shared.domain.event.RandomEventIdGenerator;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.application.commands.password.CompletePasswordReset;
import com.azat4dev.booking.users.users_commands.application.config.presentation.WebSecurityConfig;
import com.azat4dev.booking.users.users_commands.application.handlers.password.CompletePasswordResetHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.password.ResetPasswordByEmailHandler;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.core.values.IdempotentOperationId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.CompleteResetPasswordRequest;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.ResetPasswordByEmailRequest;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources.ResetPasswordController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResetPasswordController.class)
@Import(WebSecurityConfig.class)
public class ResetPasswordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Qualifier("accessTokenDecoder")
    private JwtDecoder accessTokenDecoder;

    @MockBean
    private ResetPasswordByEmailHandler resetPasswordByEmailHandler;

    @MockBean
    private CompletePasswordResetHandler completePasswordResetHandler;

    IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    @Test
    public void test_resetPasswordByEmail_givenEmail_thenPassToHandlerReturnOk() throws Exception {

        // Given
        final var request = new ResetPasswordByEmailRequest(
            anyIdempotentOperationId().toString(),
            UserHelpers.anyValidEmail().getValue()
        );

        willDoNothing().given(resetPasswordByEmailHandler)
            .handle(any());

        // When
        final var result = performResetPasswordByEmail(request);

        // Then
        result.andExpect(status().isOk());
        then(resetPasswordByEmailHandler).should(times(1))
            .handle(request);
    }

    @Test
    public void test_resetPasswordByEmail_givenEmailNotFound_thenReturnBadRequest() throws Exception {

        // Given
        final var request = new ResetPasswordByEmailRequest(
            anyIdempotentOperationId().toString(),
            "notValidEmail"
        );

        willThrow(new ResetPasswordByEmailHandler.Exception.EmailNotFound())
            .given(resetPasswordByEmailHandler).handle(any());

        // When
        final var result = performResetPasswordByEmail(request);

        // Then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("EmailNotFound"));
    }

    @Test
    public void test_completeResetPassword_givenValidToken_thenPassToHandlerReturnOk() throws Exception {

        // Given
        final var operationId = anyIdempotentOperationId();
        final var tokenValue = "tokenValue";

        final var newPassword = "newPassword";
        final var token = TokenForPasswordReset.dangerouslyMakeFrom(tokenValue);

        final var request = new CompleteResetPasswordRequest(
            operationId.toString(),
            newPassword,
            tokenValue
        );

        willDoNothing().given(resetPasswordByEmailHandler)
            .handle(any());

        // When
        final var result = performCompleteResetPassword(request);

        // Then
        result.andExpect(status().isOk());

        final var expectedCommand = new CompletePasswordReset(
            operationId.toString(),
            newPassword,
            token.getValue()
        );

        then(completePasswordResetHandler).should(times(1))
            .handle(expectedCommand);
    }

    private ResultActions performCompleteResetPassword(CompleteResetPasswordRequest request) throws Exception {
        final String url = "/api/public/set-new-password";
        return performPostRequest(
            url,
            request
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
