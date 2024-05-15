package com.azat4dev.demobooking.users.common.presentation.security.services.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class JwtDataDecoderImpl implements JwtDataDecoder {

    private final JwtDecoder jwtDecoder;

    @Override
    public Data decode(String token) throws InvalidTokenException {

        final var decoded = jwtDecoder.decode(token);

        final var claims = decoded.getClaims().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));

        return new Data(
            decoded.getSubject(),
            decoded.getIssuer().toString(),
            LocalDateTime.ofInstant(decoded.getIssuedAt(), ZoneOffset.UTC),
            LocalDateTime.ofInstant(decoded.getExpiresAt(), ZoneOffset.UTC),
            claims
        );
    }
}
