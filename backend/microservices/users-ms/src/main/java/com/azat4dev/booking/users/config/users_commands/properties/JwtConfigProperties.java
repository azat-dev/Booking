package com.azat4dev.booking.users.config.users_commands.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.security.converter.RsaKeyConverters;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;

@AllArgsConstructor
@Getter
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtConfigProperties {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    private final Duration accessTokenExpiresIn;
    private final Duration refreshTokenExpiresIn;

    @ConstructorBinding
    public JwtConfigProperties(
        File publicKey,
        File privateKey,
        Duration accessTokenExpiresIn,
        Duration refreshTokenExpiresIn
    ) throws Exception {
        this.publicKey = parsePublicKey(publicKey);
        this.privateKey = parsePrivateKey(privateKey);
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    private static RSAPublicKey parsePublicKey(File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.x509().convert(stream);
    }

    private static RSAPrivateKey parsePrivateKey(File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.pkcs8().convert(stream);
    }
}
