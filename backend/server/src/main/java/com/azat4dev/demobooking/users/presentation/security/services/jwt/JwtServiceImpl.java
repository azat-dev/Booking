package com.azat4dev.demobooking.users.presentation.security.services.jwt;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.temporal.ChronoUnit;

public final class JwtServiceImpl implements JwtService {

    private final long accessTokenExpirationInMs;
    private final long refreshTokenExpirationInMs;
    private final TimeProvider dateTimeProvider;
    private final EncodeJwt encodeJwt;
    private final JwtDecoder jwtDecoder;

    public JwtServiceImpl(
        long accessTokenExpirationInMs,
        long refreshTokenExpirationInMs,
        TimeProvider dateTimeProvider,
        EncodeJwt encodeJwt,
        JwtDecoder jwtDecoder
    ) {
        this.accessTokenExpirationInMs = accessTokenExpirationInMs;
        this.refreshTokenExpirationInMs = refreshTokenExpirationInMs;
        this.dateTimeProvider = dateTimeProvider;
        this.jwtDecoder = jwtDecoder;
        this.encodeJwt = encodeJwt;
    }

    @Override
    public String generateAccessToken(UserId userId, String[] authorities) {
        final var now = dateTimeProvider.currentTime();

        String scope = String.join(" ", authorities);

        final var claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now.toInstant())
            .expiresAt(now.toInstant().plus(accessTokenExpirationInMs, ChronoUnit.MILLIS))
            .subject(userId.toString())
            .claim("scope", scope)
            .build();

        return this.encodeJwt.execute(claims);
    }

    @Override
    public String generateRefreshToken(UserId userId, String[] authorities) {

        final var now = dateTimeProvider.currentTime();

        String scope = String.join(" ", authorities);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now.toInstant())
            .expiresAt(now.toInstant().plus(refreshTokenExpirationInMs, ChronoUnit.MILLIS))
            .subject(userId.toString())
            .claim("scope", scope)
            .build();

        return this.encodeJwt.execute(claims);
    }

    @Override
    public UserId getUserIdFromToken(String token) {

        final var decoded = jwtDecoder.decode(token);
        final var subject = decoded.getSubject();
        return UserId.fromString(subject);
    }

    @Override
    public boolean verifyToken(String authToken) {

        try {
            final var decoded = jwtDecoder.decode(authToken);
            final var expiresAt = decoded.getExpiresAt();

            final var now = dateTimeProvider.currentTime();
            assert expiresAt != null;
            return expiresAt.isAfter(now.toInstant());

        } catch (Exception e) {
            return false;
        }
    }
}