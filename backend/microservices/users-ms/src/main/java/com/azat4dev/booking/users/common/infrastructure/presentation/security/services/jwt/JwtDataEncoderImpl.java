package com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@RequiredArgsConstructor
public final class JwtDataEncoderImpl implements JwtDataEncoder {

    private final JwtEncoder jwtEncoder;

    @Override
    public String encode(
        String subject,
        String issuer,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt,
        Map<String, String> additionalClaims
    ) {

        final var claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .issuedAt(issuedAt.toInstant(ZoneOffset.UTC))
            .expiresAt(expiresAt.toInstant(ZoneOffset.UTC))
            .subject(subject)
            .claims(c -> {
                c.putAll(additionalClaims);
            })
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
