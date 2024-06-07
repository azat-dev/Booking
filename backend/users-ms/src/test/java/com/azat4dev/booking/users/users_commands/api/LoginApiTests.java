package com.azat4dev.booking.users.users_commands.api;

import com.azat4dev.booking.users.users_commands.application.commands.LoginByEmail;
import com.azat4dev.booking.users.users_commands.application.handlers.LoginByEmailHandler;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources.LoginApi;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBodyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.azat4dev.booking.users.users_commands.domain.UserHelpers.anyUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

public class LoginApiTests {

    SUT createSUT() {
        final var authenticateCurrentSession = mock(AuthenticateCurrentSession.class);
        final var handler = mock(LoginByEmailHandler.class);

        return new SUT(
            new LoginApi(
                handler,
                authenticateCurrentSession
            ),
            handler,
            authenticateCurrentSession
        );
    }

    @Test
    void test_loginByEmail_givenWrongCredentials_thenReturnError() throws Exception {

        // Given
        final var sut = createSUT();
        final var loginByEmailRequest = new com.azat4dev.booking.usersms.generated.server.model.LoginByEmailRequestBodyDTO(
            anyValidEmail(),
            "password"
        );

        willThrow(new LoginByEmailHandler.Exception.WrongCredentials())
            .given(sut.handler)
            .handle(any());


        // When
        final var exception = assertThrows(ResponseStatusException.class, () -> {
            sut.api.loginByEmail(loginByEmailRequest);
        });

        // Then
        assertThat(exception.getStatusCode().value())
            .isEqualTo(HttpStatus.FORBIDDEN.value());

        then(sut.authenticateCurrentSession).should(times(0))
            .execute(any(), any());
    }


    @Test
    void test_loginByEmail_givenNotEmptyCredentials_thenReturnError() throws Exception {

        // Given
        final var sut = createSUT();

        final var validEmail = UserHelpers.anyValidEmail();

        final var requestBody = new LoginByEmailRequestBodyDTO()
            .email(validEmail.getValue())
                .password(anyValidPassword());

        final var user = anyUser();

        final var expectedTokens = new AuthenticateCurrentSession.Tokens(
            "accessToken",
            "refreshToken"
        );

        given(sut.handler.handle(any()))
            .willReturn(user);

        given(sut.authenticateCurrentSession.execute(any(), any()))
            .willReturn(expectedTokens);


        // When
        final var response = sut.api.loginByEmail(requestBody);

        // Then
        final var expectedCommand = new LoginByEmail(
            requestBody.getEmail(),
            requestBody.getPassword()
        );

        then(sut.handler).should(times(1))
            .handle(expectedCommand);

        then(sut.authenticateCurrentSession).should(times(1))
            .execute(eq(user.getId()), any());

        final var tokens = response.getBody().getTokens();

        assertThat(tokens.getAccess())
            .isEqualTo(expectedTokens.accessToken());

        assertThat(tokens.getRefresh())
            .isEqualTo(expectedTokens.refreshToken());

        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.OK);
    }


    // Helpers
    private String anyValidEmail() {
        return "email@company.com";
    }

    private String anyValidPassword() {
        return "123456";
    }


    record SUT(
        LoginApi api,
        LoginByEmailHandler handler,
        AuthenticateCurrentSession authenticateCurrentSession
    ) {
    }
}
