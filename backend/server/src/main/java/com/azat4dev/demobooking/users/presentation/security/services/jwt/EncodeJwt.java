package com.azat4dev.demobooking.users.presentation.security.services.jwt;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;

@FunctionalInterface
public interface EncodeJwt {
    String execute(JwtClaimsSet jwt);
}
