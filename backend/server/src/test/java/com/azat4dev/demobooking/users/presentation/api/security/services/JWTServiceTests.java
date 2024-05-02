package com.azat4dev.demobooking.users.presentation.api.security.services;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JWTService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JWTServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class JWTServiceTests {

    SUT createSUT(
        long accessTokenExpirationInMs,
        long refreshTokenExpirationInMs
    ) {
        final var dateTimeProvider = mock(TimeProvider.class);

        given(dateTimeProvider.currentTime()).willReturn(new Date());

        return new SUT(
            new JWTServiceImpl(
                "secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret",
                accessTokenExpirationInMs,
                refreshTokenExpirationInMs,
                dateTimeProvider
            ),
            dateTimeProvider
        );
    }

    @Test
    void generateAccessToken_givenUserId_thenReturnToken() {

        // Given
        final var userId = UserId.generateNew();

        final var sut = createSUT(
            10000,
            10000
        );

        // When
        final var result = sut.jwtService.generateAccessToken(userId);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void generateRefreshToken_givenUserId_thenReturnToken() {

        // Given
        final var userId = UserId.generateNew();
        final var sut = createSUT(
            10000,
            10000
        );

        // When
        final var result = sut.jwtService.generateRefreshToken(userId);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void verifyToken_givenWrongToken_thenMustReturnFalse() {

        // Given
        final var wrongToken = "wrongToken";
        final var sut = createSUT(
            10000,
            10000
        );

        // When
        final var isValid = sut.jwtService.verifyToken(wrongToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void verifyToken_givenValidToken_thenSuccess() {

        // Given
        final var sut = createSUT(
            10000,
            10000
        );
        final var token = sut.jwtService.generateAccessToken(UserId.generateNew());

        // When
        final var isValid = sut.jwtService.verifyToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    void verifyToken_givenExpiredToken_thenSuccess() {

        // Given
        final var sut = createSUT(
            10000,
            10000
        );
        final var now = new Date();

        given(sut.dateTimeProvider.currentTime()).willReturn(now);

        final var token = sut.jwtService.generateAccessToken(UserId.generateNew());

        given(sut.dateTimeProvider.currentTime()).willReturn(new Date(now.getTime() + 100000000));

        // When
        final var isValid = sut.jwtService.verifyToken(token);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    void getUserIdFromToken_givenWrongToken_thenMustThrowError() {

        // Given
        final var wrongToken = "wrongToken";

        final var sut = createSUT(
            10000,
            10000
        );

        // When
        assertThrows(
            Exception.class,
            () -> sut.jwtService.getUserIdFromToken(wrongToken)
        );

        // Then
    }

    @Test
    void getUserIdFromToken_givenValidToken_thenSuccess() {

        // Given
        final var userId = UserId.generateNew();

        final var sut = createSUT(
            10000,
            10000
        );
        final var token = sut.jwtService.generateAccessToken(userId);

        // When
        final var result = sut.jwtService.getUserIdFromToken(token);

        // Then
        assertThat(result).isEqualTo(userId);
    }

    private record SUT(
        JWTService jwtService,
        TimeProvider dateTimeProvider
    ) {
    }
}
