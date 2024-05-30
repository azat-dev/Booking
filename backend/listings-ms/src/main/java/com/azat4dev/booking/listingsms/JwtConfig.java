package com.azat4dev.booking.listingsms;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPublicKey;


@Configuration
public class JwtConfig {

    @Bean
    RSAPublicKey publicKey(@Value("${app.security.jwt.publicKey}") File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.x509().convert(stream);
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
    @Qualifier("commonJwtDecoder")
    JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
