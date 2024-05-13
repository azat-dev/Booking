package com.azat4dev.demobooking.users.common.presentation.security.services.jwt;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public final class JwtServiceImpl implements JwtService {

    private final long accessTokenExpirationInMs;
    private final long refreshTokenExpirationInMs;
    private final TimeProvider dateTimeProvider;
    private final EncodeJwt encodeJwt;
    private final JwtDecoder jwtDecoder;

    @Override
    public String generateAccessToken(UserId userId, String[] authorities) {
        final var now = dateTimeProvider.currentTime();

        String scope = String.join(" ", authorities);

        final var claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now.toInstant(ZoneOffset.UTC))
            .expiresAt(now.toInstant(ZoneOffset.UTC).plus(accessTokenExpirationInMs, ChronoUnit.MILLIS))
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
            .issuedAt(now.toInstant(ZoneOffset.UTC))
            .expiresAt(now.toInstant(ZoneOffset.UTC).plus(refreshTokenExpirationInMs, ChronoUnit.MILLIS))
            .subject(userId.toString())
            .claim("scope", scope)
            .build();

        return this.encodeJwt.execute(claims);
    }

    @Override
    public UserId getUserIdFromToken(String token) throws UserId.WrongFormatException {

        final var decoded = jwtDecoder.decode(token);
        final var subject = decoded.getSubject();
        return UserId.fromString(subject);
    }

    @Override
    public boolean verifyToken(String authToken) {

        try {
            final var decoded = jwtDecoder.decode(authToken);
            final var expiresAt = decoded.getExpiresAt();

            final var now = dateTimeProvider.currentTime().toInstant(ZoneOffset.UTC);
            assert expiresAt != null;
            return expiresAt.isAfter(now);

        } catch (Exception e) {
            return false;
        }
    }
}
