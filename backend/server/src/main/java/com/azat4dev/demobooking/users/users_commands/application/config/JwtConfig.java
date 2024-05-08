package com.azat4dev.demobooking.users.users_commands.application.config;


import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.JwtServiceImpl;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.*;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
public class JwtConfig {

    @Bean
    RSAPublicKey publicKey(@Value("${app.security.jwt.publicKey}") File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.x509().convert(stream);
    }

    @Bean
    RSAPrivateKey privateKey(@Value("${app.security.jwt.privateKey}") File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.pkcs8().convert(stream);
    }

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
    public JwtEncoder jwtEncoder(
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey
    ) {
        final var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
