package com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public final class JwtServiceImpl implements JwtService {

    private final Duration accessTokenExpiresIn;
    private final Duration refreshTokenExpiresIn;
    private final JwtDataEncoder jwtDataEncoder;
    private final JwtDataDecoder jwtDataDecoder;
    private final TimeProvider dateTimeProvider;

    @Override
    public String generateAccessToken(UserId userId, String[] authorities) {
        final var now = dateTimeProvider.currentTime();
        String auth = String.join(" ", authorities);

        return jwtDataEncoder.encode(
            userId.toString(),
            "self",
            now,
            now.plus(accessTokenExpiresIn),
            Map.of(
                "type", "access",
                "scope", auth
            )
        );
    }

    @Override
    public String generateRefreshToken(UserId userId, String[] authorities) {

        final var now = dateTimeProvider.currentTime();
        String auth = String.join(" ", authorities);

        return jwtDataEncoder.encode(
            userId.toString(),
            "self",
            now,
            now.plus(refreshTokenExpiresIn),
            Map.of(
                "type", "refresh_access",
                "scope", auth
            )
        );
    }
}
