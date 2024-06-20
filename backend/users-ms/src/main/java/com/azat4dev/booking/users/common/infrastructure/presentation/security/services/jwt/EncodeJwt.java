package com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;

@FunctionalInterface
public interface EncodeJwt {
    String execute(JwtClaimsSet jwt);
}
