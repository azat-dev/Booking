package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence;


import com.azat4dev.booking.shared.data.repositories.files.MinioMediaObjectsBucket;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.users.config.users_commands.properties.UsersPhotoBucketConfigProperties;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class UsersPhotoBucketConfig {

    private final UsersPhotoBucketConfigProperties bucketConfig;

    @Bean
    @Qualifier("usersPhotoBucket")
    MediaObjectsBucket minioMediaObjectsBucket(
        @Qualifier("usersPhotoClient")
        MinioClient minioClient
    ) {
        return new MinioMediaObjectsBucket(
            bucketConfig.getBaseUrl(),
            minioClient,
            bucketConfig.getBucketName()
        );
    }

    @Bean
    @Qualifier("usersPhotoClient")
    MinioClient minioClient(
    ) {
        return MinioClient.builder()
            .endpoint(bucketConfig.getBaseUrl().toString())
            .credentials(bucketConfig.getAccessKey(), bucketConfig.getSecretKey())
            .build();
    }
}
