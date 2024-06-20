package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.files.properties;

import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.net.URL;
import java.time.Duration;

@Getter
@ConfigurationProperties(prefix = "app.objects-storage.bucket.listings-photo")
public class ListingsPhotoBucketConfigProperties {
    private final BaseUrl baseUrl;
    private final BucketName bucketName;
    private final String accessKey;
    private final String secretKey;
    private final Duration uploadUrlExpiresIn;

    @ConstructorBinding
    public ListingsPhotoBucketConfigProperties(
        @NotNull
        String name,
        URL endpoint,
        @NotNull
        @NotBlank
        String accessKey,
        @NotNull
        @NotBlank
        String secretKey,
        Duration uploadUrlExpiresIn
    ) throws BaseUrl.Exception.WrongFormatException, BucketName.Exception {
        this.baseUrl = BaseUrl.checkAndMakeFrom(endpoint);
        this.bucketName = BucketName.checkAndMake(name);
        this.accessKey = accessKey;
        this.secretKey = secretKey;

        if (uploadUrlExpiresIn.toSeconds() <= 0) {
            throw  new RuntimeException("Upload URL expiration time must be greater than 0 seconds.");
        }
        this.uploadUrlExpiresIn = uploadUrlExpiresIn;
    }
}