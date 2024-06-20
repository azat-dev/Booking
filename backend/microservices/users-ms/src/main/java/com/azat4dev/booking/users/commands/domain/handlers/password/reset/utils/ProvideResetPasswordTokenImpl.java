package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public final class ProvideResetPasswordTokenImpl implements ProvideResetPasswordToken {

    public static String TYPE = "password_reset";

    private final Duration expiresIn;
    private final JwtDataEncoder jwtDataEncoder;
    private final TimeProvider timeProvider;

    @Override
    public TokenForPasswordReset execute(UserId userId) {

        final var now = timeProvider.currentTime();

        final var token = jwtDataEncoder.encode(
            userId.toString(),
            "self",
            now,
            now.plus(expiresIn),
            Map.of("type", TYPE)
        );

        return TokenForPasswordReset.dangerouslyMakeFrom(token);
    }
}
