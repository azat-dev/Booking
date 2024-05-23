package com.azat4dev.booking.users.common.presentation.security.services.jwt;

import java.time.LocalDateTime;
import java.util.Map;

public interface JwtDataEncoder {

    String encode(
        String subject,
        String issuer,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt,
        Map<String, String> claims
    );
}
