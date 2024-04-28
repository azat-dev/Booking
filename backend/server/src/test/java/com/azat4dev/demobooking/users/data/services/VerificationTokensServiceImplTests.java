package com.azat4dev.demobooking.users.data.services;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.services.*;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class VerificationTokensServiceImplTests {

    record SUT (
        VerificationTokensService service,
        TimeProvider timeProvider,
        long expirationTimeMs
    ) {}

    SUT createSUT() {
        final var expirationTimeMs = 1000000L;
        final var timeProvider = mock(TimeProvider.class);

        return new SUT(
            new VerificationTokensServiceImpl(
                "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret",
                expirationTimeMs,
                timeProvider
            ),
            timeProvider,
            expirationTimeMs
        );
    }

    @Test
    void test_given_valid_data__when_makeVerificationToken_and_parse__then_tokens_should_match() throws ExpiredVerificationToken, WrongFormatOfVerificationToken {

        // Given
        final var sut = createSUT();
        final var userId = UserId.generateNew();
        final var time = new Date();

        given(sut.timeProvider.currentTime()).willReturn(time);

        // When
        final var token = sut.service.makeVerificationToken(userId);

        // Then
        assertThat(token).isNotNull();

        // When
        final var parsedUserId = sut.service.parse(token);

        // Then
        assertThat(parsedUserId).isEqualTo(userId);
    }

    @Test
    void test_given_expired_token__when_parse__then_throw_exception() throws ExpiredVerificationToken, WrongFormatOfVerificationToken {

        // Given
        final var sut = createSUT();
        final var userId = UserId.generateNew();
        final var time = new Date();

        given(sut.timeProvider.currentTime()).willReturn(time);

        // When
        final var token = sut.service.makeVerificationToken(userId);

        // Then
        sut.service.parse(token);

        final var nextTime = new Date(time.getTime() + sut.expirationTimeMs + 1000);
        given(sut.timeProvider.currentTime()).willReturn(nextTime);

        // When
        assertThrows(ExpiredVerificationToken.class, () -> sut.service.parse(token));
    }

    @Test
    void test_given_wrong_token__when_parse__then_throw_exception() throws ExpiredVerificationToken, WrongFormatOfVerificationToken {

        // Given
        final var sut = createSUT();
        final var invalidToken = new VerificationToken("invalidToken");

        given(sut.timeProvider.currentTime()).willReturn(new Date());

        // When
        final var exception = assertThrows(WrongFormatOfVerificationToken.class, () -> sut.service.parse(invalidToken));

        // Then
        assertThat(exception).isInstanceOf(WrongFormatOfVerificationToken.class);
    }
}
