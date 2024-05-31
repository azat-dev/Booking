package com.azat4dev.booking.users.common.presentation.security.services.jwt;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Map;

@RequiredArgsConstructor
public final class JwtServiceImpl implements JwtService {

    private final long accessTokenExpirationInMs;
    private final long refreshTokenExpirationInMs;
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
            now.plus(accessTokenExpirationInMs, ChronoUnit.MILLIS),
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
            now.plus(refreshTokenExpirationInMs, ChronoUnit.MILLIS),
            Map.of(
                "type", "refresh_access",
                "scope", auth
            )
        );
    }
}
