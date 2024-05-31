package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Map;

@RequiredArgsConstructor
public final class ProvideResetPasswordTokenImpl implements ProvideResetPasswordToken {

    public static String TYPE = "password_reset";

    private final long expirationInMs;
    private final JwtDataEncoder jwtDataEncoder;
    private final TimeProvider timeProvider;

    @Override
    public TokenForPasswordReset execute(UserId userId) {

        final var now = timeProvider.currentTime();

        final var token = jwtDataEncoder.encode(
            userId.toString(),
            "self",
            now,
            now.plus(expirationInMs, ChronoUnit.MILLIS),
            Map.of("type", TYPE)
        );

        return TokenForPasswordReset.dangerouslyMakeFrom(token);
    }
}
