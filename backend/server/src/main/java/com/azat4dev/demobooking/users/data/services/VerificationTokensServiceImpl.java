package com.azat4dev.demobooking.users.data.services;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.services.ExpiredVerificationToken;
import com.azat4dev.demobooking.users.domain.services.VerificationToken;
import com.azat4dev.demobooking.users.domain.services.VerificationTokensService;
import com.azat4dev.demobooking.users.domain.services.WrongFormatOfVerificationToken;
import com.azat4dev.demobooking.users.domain.values.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;

import java.util.Date;

public final class VerificationTokensServiceImpl implements VerificationTokensService {

    private final SecretKey secretKey;
    private final TimeProvider dateTimeProvider;
    private final long expirationTimeInMs;

    public VerificationTokensServiceImpl(
        String jwtSecret,
        long expirationTimeInMs,
        TimeProvider timeProvider
    ) {

        if (jwtSecret == null) {
            throw new IllegalArgumentException("jwtSecret must not be null");
        }

        if (timeProvider == null) {
            throw new IllegalArgumentException("timeProvider must not be null");
        }

        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.expirationTimeInMs = expirationTimeInMs;
        this.dateTimeProvider = timeProvider;
    }

    @Override
    public VerificationToken makeVerificationToken(UserId userId) {

        final var now = this.dateTimeProvider.currentTime();
        Date expiryDate = new Date(now.getTime() + this.expirationTimeInMs);

        return new VerificationToken(
            Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(
                    secretKey,
                    Jwts.SIG.HS512
                )
                .compact()
        );
    }

    @Override
    public UserId parse(VerificationToken token) throws ExpiredVerificationToken, WrongFormatOfVerificationToken {
        try {
            final var encodedUserId = Jwts.parser()
                .clock(dateTimeProvider::currentTime)
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token.getValue())
                .getPayload()
                .getSubject();

           return UserId.fromString(encodedUserId);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredVerificationToken();
        } catch (Exception ex) {
            throw new WrongFormatOfVerificationToken();
        }
    }
}