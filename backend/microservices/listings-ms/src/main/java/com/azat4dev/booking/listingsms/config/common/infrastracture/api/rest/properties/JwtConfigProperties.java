package com.azat4dev.booking.listingsms.config.common.infrastracture.api.rest.properties;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.FileInputStream;
import java.security.interfaces.RSAPublicKey;

@Validated
@Data
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtConfigProperties {

    private final RSAPublicKey publicKey;

    @ConstructorBinding
    public JwtConfigProperties(@Nonnull File publicKey) throws Exception {
        this.publicKey = parsePublicKey(publicKey);
    }

    private static RSAPublicKey parsePublicKey(File file) throws Exception {
        final var stream = new FileInputStream(file);
        return RsaKeyConverters.x509().convert(stream);
    }
}
