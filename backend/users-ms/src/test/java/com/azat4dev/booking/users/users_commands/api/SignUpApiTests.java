package com.azat4dev.booking.users.users_commands.api;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.application.ErrorDTO;
import com.azat4dev.booking.users.users_commands.application.commands.SignUp;
import com.azat4dev.booking.users.users_commands.application.handlers.SignUpHandler;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources.SignUpApi;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.usersms.generated.server.model.FullNameDTO;
import com.azat4dev.booking.usersms.generated.server.model.SignUpByEmailRequestBody;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.azat4dev.booking.users.users_commands.domain.UserHelpers.anyValidUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

public class SignUpApiTests {

    SUT createSUT() {
        final var authenticateCurrentSession = mock(AuthenticateCurrentSession.class);
        final var handler = mock(SignUpHandler.class);

        return new SUT(
            new SignUpApi(
                handler,
                authenticateCurrentSession
            ),
            handler,
            authenticateCurrentSession
        );
    }

    @Test
    void test_singUp_givenUserExists_thenReturnError() throws Exception {

        // Given
        final var sut = createSUT();

        final var validPassword = anyValidPassword();
        final var validEmail = anyValidEmail();
        final var fullName = anyValidFullName();

        willThrow(new SignUpHandler.Exception.UserWithSameEmailAlreadyExists())
            .given(sut.handler)
            .handle(any(SignUp.class));

        final var request = new SignUpByEmailRequestBody(
            validEmail,
            validPassword,
            fullName
        );

        // When
        final var exception = assertThrows(
            ControllerException.class,
            () -> sut.api.signUpByEmail(request)
        );

        // Then
        assertThat(exception.status())
            .isEqualTo(HttpStatus.CONFLICT);

        assertThat(exception.body()).isInstanceOf(ErrorDTO.class);
    }

    @Test
    void test_singUp_givenValidCredentials_thenCreateANewUser() throws Exception {

        // Given
        final var sut = createSUT();
        final var userId = anyValidUserId();
        final var fullName = anyValidFullName();
        final var validEmail = anyValidEmail();
        final var password = Password.checkAndMakeFromString("password");

        final var expectedAccessToken = "accessToken";
        final var expectedRefreshToken = "refreshToken";

        final var request = new SignUpByEmailRequestBody(
            validEmail,
            password.getValue(),
            fullName
        );

        final var expectedTokens = new AuthenticateCurrentSession.Tokens(
            expectedAccessToken,
            expectedRefreshToken
        );

        given(sut.handler.handle(any()))
            .willReturn(userId);

        given(sut.authenticateCurrentSession.execute(any(), any()))
            .willReturn(expectedTokens);

        // When
        final var response = sut.api.signUpByEmail(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final var responseBody = response.getBody();

        assertThat(responseBody.getUserId())
            .isEqualTo(userId.value());

        assertThat(responseBody.getTokens().getAccess())
            .isEqualTo(expectedAccessToken);

        assertThat(responseBody.getTokens().getRefresh())
            .isEqualTo(expectedRefreshToken);

        then(sut.authenticateCurrentSession).should(times(1))
            .execute(
                eq(userId),
                any()
            );
    }

    private String anyValidPassword() {
        return "123456";
    }

    // Helpers

    private String anyValidEmail() {
        return "email@company.com";
    }

    private FullNameDTO anyValidFullName() {
        final var fullName = UserHelpers.anyFullName();

        return new FullNameDTO(
            fullName.getFirstName().getValue(),
            fullName.getLastName().getValue()
        );
    }

    record SUT(
        SignUpApi api,
        SignUpHandler handler,
        AuthenticateCurrentSession authenticateCurrentSession
    ) {
    }
}
