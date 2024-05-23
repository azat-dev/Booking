package com.azat4dev.booking.users.users_commands.application.config;


import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.*;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Qualifier;
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
    EncodeJwt encodeJwt(JwtEncoder jwtEncoder) {
        return new EncodeJwt() {
            @Override
            public String execute(JwtClaimsSet claims) {
                return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            }
        };
    }

    @Bean
    public JwtService jwtService(
        TimeProvider timeProvider,
        @Value("${app.security.jwt.accessToken.expirationInMs}") long jwtAccessTokenExpirationInMs,
        @Value("${app.security.jwt.refreshToken.expirationInMs}") long jwtRefreshTokenExpirationInMs,
        JwtDataEncoder jwtDataEncoder,
        JwtDataDecoder jwtDecoder
    ) {
        return new JwtServiceImpl(
            jwtAccessTokenExpirationInMs,
            jwtRefreshTokenExpirationInMs,
            jwtDataEncoder,
            jwtDecoder,
            timeProvider
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        final var jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("commonJwtDecoder")
    JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    @Qualifier("accessTokenDecoder")
    JwtDecoder accessTokenDecoder(JwtDecoder decoder) {

        return token -> {
            final var jwt = decoder.decode(token);

            if (jwt.getClaim("type").equals("access")) {
                return jwt;
            }

            throw new JwtException("Invalid token type");
        };
    }

    @Bean
    public JwtDataDecoder jwtDataDecoder(@Qualifier("commonJwtDecoder") JwtDecoder decoder) {
        return new JwtDataDecoderImpl(decoder);
    }

    @Bean
    public JwtDataEncoder jwtDataEncoder(JwtEncoder jwtEncoder) {
        return new JwtDataEncoderImpl(jwtEncoder);
    }

}
