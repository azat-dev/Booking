package com.azat4dev.demobooking.users.presentation.api.security.services;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.EncodeJwt;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import static com.azat4dev.demobooking.users.domain.UserHelpers.anyValidUserId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


class JwtServiceTests {

    private static void assertUserIdEncoded(SUT sut, UserId userId) {
        then(sut.encodeJwt).should(times(1))
            .execute(assertArg(v -> {
                assertThat(v.getSubject()).isEqualTo(userId.toString());
            }));
    }

    private static Jwt anyJwt(String userId, Instant createdAt, Instant expiresAt) {
        final var headers = new HashMap<String, Object>();
        headers.put("header1", "value1");

        final var claims = new HashMap<String, Object>();
        claims.put("sub", userId);

        return new Jwt(
            "token",
            createdAt,
            expiresAt,
            headers,
            claims
        );
    }

    SUT createSUT() {
        final var dateTimeProvider = mock(TimeProvider.class);

        given(dateTimeProvider.currentTime()).willReturn(new Date());

        final var encoder = mock(EncodeJwt.class);
        final var decoder = mock(JwtDecoder.class);

        return new SUT(
            new JwtServiceImpl(
                10000,
                10000,
                dateTimeProvider,

                encoder,
                decoder
            ),
            dateTimeProvider,
            encoder,
            decoder
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

        given(sut.encodeJwt.execute(any()))
            .willReturn(expectedToken);

        // When
        final var result = sut.service.generateAccessToken(userId, authorities);

        // Then
        assertUserIdEncoded(sut, userId);
        assertThat(result).isEqualTo(expectedToken);
    }

    @Test
    void generateRefreshToken_givenUserId_thenReturnToken() {

        // Given
        final var userId = anyValidUserId();
        final var sut = createSUT();
        final var authorities = anyAuthorities();
        final var expectedToken = "token";

        given(sut.encodeJwt.execute(any()))
            .willReturn(expectedToken);

        // When
        final var result = sut.service.generateRefreshToken(userId, authorities);

        // Then
        assertUserIdEncoded(sut, userId);
        assertThat(result).isEqualTo(expectedToken);
    }

    @Test
    void verifyToken_givenWrongToken_thenMustReturnFalse() {

        // Given
        final var wrongToken = "wrongToken";
        final var sut = createSUT();

        given(sut.decoder.decode(any())).willThrow(
            new RuntimeException()
        );

        // When
        final var isValid = sut.service.verifyToken(wrongToken);

        // Then
        then(sut.decoder).should(times(1))
            .decode(wrongToken);
        assertThat(isValid).isFalse();
    }

    @Test
    void verifyToken_givenValidToken_thenSuccess() {

        // Given
        final var sut = createSUT();
        final var token = "token";
        final var validJwt = anyJwt(
            "userId",
            Instant.now(),
            Instant.now().plusSeconds(10000)
        );

        given(sut.decoder.decode(any())).willReturn(validJwt);

        // When
        final var isValid = sut.service.verifyToken(token);

        // Then
        then(sut.decoder).should(times(1))
            .decode(token);

        assertThat(isValid).isTrue();
    }

    @Test
    void verifyToken_givenExpiredToken_thenSuccess() {

        // Given
        final var sut = createSUT();
        final var now = new Date();
        final var token = "token";
        final var userId = anyValidUserId();

        final var expiredJwt = anyJwt(
            userId.toString(),
            now.toInstant(),
            now.toInstant().plusSeconds(100)
        );

        given(sut.dateTimeProvider.currentTime())
            .willReturn(new Date(now.getTime() + 100000));

        given(sut.decoder.decode(any()))
            .willReturn(expiredJwt);

        // When
        final var isValid = sut.service.verifyToken(token);

        // Then
        then(sut.decoder).should(times(1))
            .decode(token);
        assertThat(isValid).isFalse();
    }

    @Test
    void getUserIdFromToken_givenWrongToken_thenMustThrowError() {

        // Given
        final var wrongToken = "wrongToken";
        final var sut = createSUT();

        given(sut.decoder.decode(any()))
            .willThrow(new RuntimeException());

        // When
        assertThrows(
            Exception.class,
            () -> sut.service.getUserIdFromToken(wrongToken)
        );

        // Then
    }

    @Test
    void getUserIdFromToken_givenValidToken_thenSuccess() {

        // Given
        final var userId = anyValidUserId();
        final var authorities = anyAuthorities();

        final var sut = createSUT();
        final var token = sut.service.generateAccessToken(userId, authorities);

        final var validJwt = anyJwt(
            userId.toString(),
            Instant.now(),
            Instant.now().plusSeconds(1000)
        );

        given(sut.decoder.decode(any())).willReturn(validJwt);

        // When
        final var result = sut.service.getUserIdFromToken(token);

        // Then
        assertThat(result).isEqualTo(userId);
    }

    private record SUT(
        JwtService service,
        TimeProvider dateTimeProvider,
        EncodeJwt encodeJwt,
        JwtDecoder decoder
    ) {
    }
}
