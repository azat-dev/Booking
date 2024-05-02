package com.azat4dev.demobooking.users.application.config;


import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JwtServiceImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
public class JwtConfig {

    @Value("${jwt.publicKey}")
    RSAPublicKey publicKey;

    @Value("${jwt.privateKey}")
    RSAPrivateKey privateKey;

    @Bean
    public JwtService jwtService(
        TimeProvider timeProvider,
        @Value("${app.security.jwt.accessToken.expirationInMs}") long jwtAccessTokenExpirationInMs,
        @Value("${app.security.jwt.refreshToken.expirationInMs}") long jwtRefreshTokenExpirationInMs,
        JwtEncoder jwtEncoder,
        JwtDecoder jwtDecoder
    ) {
        return new JwtServiceImpl(
            jwtAccessTokenExpirationInMs,
            jwtRefreshTokenExpirationInMs,
            timeProvider,
            claims -> {
                return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            },
            jwtDecoder
        );
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        final var jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }
}
