package com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt;

import java.time.LocalDateTime;
import java.util.Map;

public interface JwtDataDecoder {

    Data decode(String token) throws InvalidTokenException;

    record Data(
        String subject,
        String issuer,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt,
        Map<String, String> additionalClaims
    ) {
    }

    // Exceptions

    final class InvalidTokenException extends RuntimeException {
    }
}
