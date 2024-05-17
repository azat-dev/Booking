package com.azat4dev.demobooking.users.users_commands.data.services.password;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.GetInfoForPasswordResetToken;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserIdImpl;
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
    void test_execute_givenNotValidToken_thenThrowException() throws GetInfoForPasswordResetToken.Exception.InvalidToken {
        // Given
        final var sut = createSUT();
        final var token = TokenForPasswordReset.dangerouslyMakeFrom("token");

        given(sut.getTokenInfo.execute(any()))
            .willThrow(new GetInfoForPasswordResetToken.Exception.InvalidToken());

        // When
        final var exception = assertThrows(
            ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken.class,
            () -> sut.validate.execute(token)
        );

        // Then
        assertThat(exception)
            .isInstanceOf(ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken.class);
    }

    @Test
    void test_execute_givenExpiredToken_thenThrowException() throws GetInfoForPasswordResetToken.Exception.InvalidToken {
        // Given
        final var sut = createSUT();
        final var token = TokenForPasswordReset.dangerouslyMakeFrom("token");
        final var userId = UserHelpers.anyValidUserId();
        final var expiresAt = LocalDateTime.now();

        given(sut.timeProvider.currentTime())
            .willReturn(expiresAt.plusDays(1));


        given(sut.getTokenInfo.execute(any()))
            .willReturn(new GetInfoForPasswordResetToken.Data(
                userId,
                expiresAt
            ));

        // When
        final var exception = assertThrows(
            ValidateTokenForPasswordResetAndGetUserId.Exception.TokenExpired.class,
            () -> sut.validate.execute(token)
        );

        // Then
        assertThat(exception)
            .isInstanceOf(ValidateTokenForPasswordResetAndGetUserId.Exception.TokenExpired.class);
    }

    @Test
    void test_execute_givenValidToken_thenReturnUserId() throws GetInfoForPasswordResetToken.Exception.InvalidToken {
        // Given
        final var sut = createSUT();
        final var token = TokenForPasswordReset.dangerouslyMakeFrom("token");
        final var userId = UserHelpers.anyValidUserId();
        final var expiresAt = LocalDateTime.now();

        given(sut.timeProvider.currentTime())
            .willReturn(expiresAt.minusDays(1));

        given(sut.getTokenInfo.execute(any()))
            .willReturn(new GetInfoForPasswordResetToken.Data(
                userId,
                expiresAt
            ));

        // When
        final UserId resultUserId;
        try {
            resultUserId = sut.validate.execute(token);
        } catch (ValidateTokenForPasswordResetAndGetUserId.Exception e) {
            throw new RuntimeException(e);
        }

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
