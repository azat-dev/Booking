package com.azat4dev.demobooking.users.users_commands.application.config.data;

import com.azat4dev.demobooking.users.users_commands.data.repositories.MinioMediaObjectsBucket;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MediaObjectsBucketConfig {

    @Bean
    @Qualifier("usersPhotoBucket")
    MinioMediaObjectsBucket minioMediaObjectsBucket(
        @Qualifier("usersPhotoClient")
        MinioClient minioClient,
        @Value("${app.objects_storage.bucket.users-photo.name}") String bucketName
    ) {
        return new MinioMediaObjectsBucket(minioClient, bucketName);
    }

    @Bean
    @Qualifier("usersPhotoClient")
    MinioClient minioClient(
        @Value("${app.objects_storage.bucket.users-photo.endpoint}") String endPoint,
        @Value("${app.objects_storage.bucket.users-photo.access-key}") String accessKey,
        @Value("${app.objects_storage.bucket.users-photo.secret-key}") String secretKey
    ) {
        return MinioClient.builder()
            .endpoint(endPoint)
            .credentials(accessKey, secretKey)
            .build();
    }
}
