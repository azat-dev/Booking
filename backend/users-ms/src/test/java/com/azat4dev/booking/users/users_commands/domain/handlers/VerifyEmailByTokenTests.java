package com.azat4dev.booking.users.users_commands.domain.handlers;

import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationTokenInfo;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByTokenImpl;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils.GetInfoForEmailVerificationToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class VerifyEmailByTokenTests {

    SUT createSUT() {

        final var getTokenInfo = mock(GetInfoForEmailVerificationToken.class);
        final var users = mock(Users.class);

        final var timeProvider = new SystemTimeProvider();

        return new SUT(
            new VerifyEmailByTokenImpl(
                getTokenInfo,
                users,
                timeProvider
            ),
            getTokenInfo,
            users,
            timeProvider
        );
    }

    EmailVerificationToken anyEmailVerificationToken() {
        return new EmailVerificationToken("any");
    }

    @Test
    void test_handle_givenTokenIsNotValid_thenThrowException() {

        // Given
        final var sut = createSUT();
        final var token = anyEmailVerificationToken();

        willThrow(new GetInfoForEmailVerificationToken.TokenIsNotValidException())
            .given(sut.getTokenInfo)
            .execute(any());

        // When
        final var exception = assertThrows(VerifyEmailByToken.Exception.TokenIsNotValid.class, () -> {
            sut.verifyEmailByToken.execute(token);
        });

        // Then
        assertThat(exception).isNotNull();
    }

    @Test
    void test_handle_givenValidToken_thenSetUserVerificationStatusAndPublishEvent() throws Exception {

        // Given
        final var sut = createSUT();
        final var existingUser = UserHelpers.anyUser();
        final var token = anyEmailVerificationToken();
        final var currentTime = LocalDateTime.now();

        final var tokenInfo = new EmailVerificationTokenInfo(
            existingUser.getId(),
            existingUser.getEmail(),
            currentTime.plusDays(100)
        );

        given(sut.getTokenInfo.execute(any()))
            .willReturn(tokenInfo);

        willDoNothing().given(sut.users)
            .addVerifiedEmail(any(), any());

        // When
        sut.verifyEmailByToken.execute(token);

        // Then
        then(sut.getTokenInfo).should(times(1))
            .execute(token);

        then(sut.users)
            .should(times(1))
            .addVerifiedEmail(tokenInfo.userId(), tokenInfo.email());
    }

    record SUT(
        VerifyEmailByToken verifyEmailByToken,
        GetInfoForEmailVerificationToken getTokenInfo,
        Users users,
        TimeProvider timeProvider
    ) {
    }
}
