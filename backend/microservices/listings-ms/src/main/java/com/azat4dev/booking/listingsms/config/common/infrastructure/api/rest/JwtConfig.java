package com.azat4dev.booking.listingsms.config.common.infrastructure.api.rest;


import com.azat4dev.booking.listingsms.config.common.infrastructure.api.rest.properties.JwtConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;


@AllArgsConstructor
@EnableConfigurationProperties(JwtConfigProperties.class)
@Configuration
public class JwtConfig {

    private final JwtConfigProperties jwtConfig;

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
    @Qualifier("commonJwtDecoder")
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(jwtConfig.getPublicKey()).build();
    }
}
