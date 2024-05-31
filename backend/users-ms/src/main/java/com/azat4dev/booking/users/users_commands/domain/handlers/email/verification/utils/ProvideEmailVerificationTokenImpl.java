package com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Map;

@RequiredArgsConstructor
public final class ProvideEmailVerificationTokenImpl implements ProvideEmailVerificationToken {

    public static final String TYPE = "email_verification";

    private final long tokenExpirationInMs;
    private final JwtDataEncoder jwtDataEncoder;
    private final TimeProvider dateTimeProvider;

    @Override
    public EmailVerificationToken execute(UserId userId, EmailAddress emailAddress) {

        final var now = dateTimeProvider.currentTime();

        final var token = jwtDataEncoder.encode(
            userId.toString(),
            "self",
            now,
            now.plus(tokenExpirationInMs, ChronoUnit.MILLIS),
            Map.of(
                "email", emailAddress.getValue(),
                "type", TYPE
            )
        );

        return new EmailVerificationToken(token);
    }
}
