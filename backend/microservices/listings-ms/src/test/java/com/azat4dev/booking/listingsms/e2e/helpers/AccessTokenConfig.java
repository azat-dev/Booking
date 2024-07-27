package com.azat4dev.booking.listingsms.e2e.helpers;

import com.azat4dev.booking.listingsms.config.common.infrastructure.api.rest.properties.JwtConfigProperties;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;

@TestConfiguration
public class AccessTokenConfig {
    @Bean
    public RSAPrivateKey privateKey(@Value("${app.security.jwt.privateKey}") File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.pkcs8().convert(stream);
    }

    @Bean
    public JwtEncoder jwtEncoder(JwtConfigProperties jwtConfigProperties, RSAPrivateKey privateKey) {
        final var jwk = new RSAKey.Builder(jwtConfigProperties.getPublicKey()).privateKey(privateKey).build();
        final var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public GenerateAccessToken generateAccessToken(JwtEncoder jwtEncoder) {
        return new GenerateAccessToken() {
            @Override
            public String execute(UserId userId) {
                return jwtEncoder.encode(
                    JwtEncoderParameters.from(
                        JwtClaimsSet.builder()
                            .subject(userId.toString())
                            .claim("type", "access")
                            .expiresAt(Instant.now().plusSeconds(3600)
                            ).build()
                    )
                ).getTokenValue();
            }
        };
    }
}
