package com.azat4dev.booking.users.config.users_commands;


import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.*;
import com.azat4dev.booking.users.config.users_commands.properties.JwtConfigProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

@AllArgsConstructor
@Configuration
public class JwtConfig {

    private final JwtConfigProperties jwtConfig;

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
        JwtDataEncoder jwtDataEncoder
    ) {
        return new JwtServiceImpl(
            jwtConfig.getAccessTokenExpiresIn(),
            jwtConfig.getRefreshTokenExpiresIn(),
            jwtDataEncoder,
            timeProvider
        );
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        final var jwk = new RSAKey.Builder(jwtConfig.getPublicKey())
            .privateKey(jwtConfig.getPrivateKey())
            .build();
        final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Qualifier("commonJwtDecoder")
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(jwtConfig.getPublicKey()).build();
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
