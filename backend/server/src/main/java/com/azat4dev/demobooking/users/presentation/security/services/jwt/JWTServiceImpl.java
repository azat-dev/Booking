package com.azat4dev.demobooking.users.presentation.security.services.jwt;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.values.UserId;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;

public final class JWTServiceImpl implements JWTService {

    private final SecretKey secretKey;
    private final long accessTokenExpirationInMs;
    private final long refreshTokenExpirationInMs;
    private final TimeProvider dateTimeProvider;

    public JWTServiceImpl(
        String jwtSecret,
        long accessTokenExpirationInMs,
        long refreshTokenExpirationInMs,
        TimeProvider dateTimeProvider
    ) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.accessTokenExpirationInMs = accessTokenExpirationInMs;
        this.refreshTokenExpirationInMs = refreshTokenExpirationInMs;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public String generateAccessToken(UserId userId) {
        final var now = dateTimeProvider.currentTime();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationInMs);

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(
                secretKey,
                Jwts.SIG.HS512
            )
            .compact();
    }

    @Override
    public String generateRefreshToken(UserId userId) {
        final var now = dateTimeProvider.currentTime();
        final var expiryDate = new Date(now.getTime() + refreshTokenExpirationInMs);

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(
                secretKey,
                Jwts.SIG.HS512
            )
            .compact();
    }

    @Override
    public UserId getUserIdFromToken(String token) {

        final var encodedUserId = Jwts.parser()
            .clock(dateTimeProvider::currentTime)
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();

        return UserId.fromString(encodedUserId);
    }

    @Override
    public boolean verifyToken(String authToken) {
        try {
            Jwts.parser()
                .clock(dateTimeProvider::currentTime)
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException ex) {
            // log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // log.error("JWT claims string is empty.");
        }
        return false;
    }
}
