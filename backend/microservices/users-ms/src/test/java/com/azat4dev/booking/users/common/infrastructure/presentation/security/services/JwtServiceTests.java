package com.azat4dev.booking.users.common.infrastructure.presentation.security.services;

import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataDecoder;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtService;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Map;

import static com.azat4dev.booking.users.commands.domain.UserHelpers.anyValidUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


class JwtServiceTests {

    SUT createSUT() {

        final var timeProvider = new SystemTimeProvider();
        final var dateTimeProvider = mock(TimeProvider.class);

        given(dateTimeProvider.currentTime())
            .willReturn(timeProvider.currentTime());

        final var encoder = Mockito.mock(JwtDataEncoder.class);

        return new SUT(
            new JwtServiceImpl(
                Duration.ofSeconds(10),
                Duration.ofSeconds(10),
                encoder,
                dateTimeProvider
            ),
            dateTimeProvider,
            encoder
        );
    }

    String[] anyAuthorities() {
        return new String[]{"USER"};
    }

    @Test
    void generateAccessToken_givenUserId_thenReturnToken() {

        // Given
        final var userId = anyValidUserId();

        final var sut = createSUT();
        final var authorities = anyAuthorities();
        final var expectedToken = "token";

        given(sut.encoder.encode(any(), any(), any(), any(), any()))
            .willReturn(expectedToken);

        // When
        final var result = sut.service.generateAccessToken(userId, authorities);

        // Then
        Assertions.assertThat(result).isEqualTo(expectedToken);

        then(sut.encoder).should(times(1))
            .encode(
                userId.toString(),
                "self",
                sut.dateTimeProvider.currentTime(),
                sut.dateTimeProvider.currentTime().plusSeconds(10),
                Map.of(
                    "scope", String.join(" ", authorities),
                    "type", "access"
                )
            );
    }

    @Test
    void generateRefreshToken_givenUserId_thenReturnToken() {

        // Given
        final var userId = anyValidUserId();
        final var sut = createSUT();
        final var authorities = anyAuthorities();
        final var expectedToken = "token";

        given(sut.encoder.encode(any(), any(), any(), any(), any()))
            .willReturn(expectedToken);

        // When
        final var result = sut.service.generateRefreshToken(userId, authorities);

        // Then

        then(sut.encoder).should(times(1))
            .encode(
                userId.toString(),
                "self",
                sut.dateTimeProvider.currentTime(),
                sut.dateTimeProvider.currentTime().plusSeconds(10),
                Map.of(
                    "scope", String.join(" ", authorities),
                    "type", "refresh_access"
                )
            );

        Assertions.assertThat(result).isEqualTo(expectedToken);
    }

    private record SUT(
        JwtService service,
        TimeProvider dateTimeProvider,
        JwtDataEncoder encoder
    ) {
    }
}
