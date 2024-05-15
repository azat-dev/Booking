package com.azat4dev.demobooking.users.users_commands.data.services.password;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.PasswordResetToken;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.ValidateTokenForPasswordResetAndGetUserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ValidateTokenForPasswordResetAndGetUserIdImplTests {

    SUT createSUT() {
        final var getTokenInfo = mock(GetInfoForPasswordResetToken.class);
        final var timeProvider = mock(TimeProvider.class);

        return new SUT(
            new ValidateTokenForPasswordResetAndGetUserIdImpl(
                getTokenInfo,
                timeProvider
            ),
            getTokenInfo,
            timeProvider
        );
    }

    @Test
    void test_execute_givenNotValidToken_thenThrowException() {
        // Given
        final var sut = createSUT();
        final var token = PasswordResetToken.dangerouslyMakeFrom("token");

        given(sut.getTokenInfo.execute(any()))
            .willThrow(new GetInfoForPasswordResetToken.InvalidTokenException());

        // When
        final var exception = assertThrows(
            ValidateTokenForPasswordResetAndGetUserId.InvalidTokenException.class,
            () -> sut.validate.execute(token)
        );

        // Then
        assertThat(exception)
            .isInstanceOf(ValidateTokenForPasswordResetAndGetUserId.InvalidTokenException.class);
    }

    @Test
    void test_execute_givenValidToken_thenReturnUserId() {
        // Given
        final var sut = createSUT();
        final var token = PasswordResetToken.dangerouslyMakeFrom("token");
        final var userId = UserHelpers.anyValidUserId();
        final var expiresAt = LocalDateTime.now();

        given(sut.getTokenInfo.execute(any()))
            .willReturn(new GetInfoForPasswordResetToken.Data(
                userId,
                expiresAt
            ));

        // When
        final var resultUserId = sut.validate.execute(token);

        // Then
        assertThat(resultUserId).isEqualTo(userId);
    }

    record SUT(
        ValidateTokenForPasswordResetAndGetUserIdImpl validate,
        GetInfoForPasswordResetToken getTokenInfo,
        TimeProvider timeProvider
    ) {
    }
}
