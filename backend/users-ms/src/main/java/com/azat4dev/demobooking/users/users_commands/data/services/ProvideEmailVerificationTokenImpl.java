package com.azat4dev.demobooking.users.users_commands.data.services;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.EncodeJwt;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.ProvideEmailVerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public final class ProvideEmailVerificationTokenImpl implements ProvideEmailVerificationToken  {

    private final long tokenExpirationInMs;
    private final EncodeJwt encodeJwt;
    private final TimeProvider dateTimeProvider;

    @Override
    public EmailVerificationToken execute(UserId userId, EmailAddress emailAddress) {

        final var now = dateTimeProvider.currentTime();

        final var claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now.toInstant(ZoneOffset.UTC))
            .expiresAt(now.toInstant(ZoneOffset.UTC).plus(tokenExpirationInMs, ChronoUnit.MILLIS))
            .subject(userId.toString())
            .claim("email", emailAddress.getValue())
            .build();

        return new EmailVerificationToken(
            encodeJwt.execute(claims)
        );
    }
}
