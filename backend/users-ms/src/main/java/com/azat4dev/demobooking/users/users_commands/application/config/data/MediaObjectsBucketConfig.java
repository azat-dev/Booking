package com.azat4dev.demobooking.users.users_commands.application.config.data;

import com.azat4dev.demobooking.users.users_commands.data.repositories.MinioMediaObjectsBucket;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.BucketName;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class MediaObjectsBucketConfig {

    @Value("${app.objects_storage.bucket.users-photo.endpoint}")
    URL userPhotosBucketEndpoint;

    @Bean("usersPhotoBucketName")
    BucketName usersPhotoBucketName(
        @Value("${app.objects_storage.bucket.users-photo.name}")
        String bucketName
    ) {
        return BucketName.makeWithoutChecks(bucketName);
    }


    @Bean
    @Qualifier("usersPhotoBucket")
    MinioMediaObjectsBucket minioMediaObjectsBucket(
        @Qualifier("usersPhotoClient")
        MinioClient minioClient,
        @Qualifier("usersPhotoBucketName")
        BucketName bucketName
    ) {
        return new MinioMediaObjectsBucket(
            userPhotosBucketEndpoint,
            minioClient,
            bucketName
        );
    }

    @Bean
    @Qualifier("usersPhotoClient")
    MinioClient minioClient(
        @Value("${app.objects_storage.bucket.users-photo.access-key}") String accessKey,
        @Value("${app.objects_storage.bucket.users-photo.secret-key}") String secretKey
    ) {
        return MinioClient.builder()
            .endpoint(userPhotosBucketEndpoint.toString())
            .credentials(accessKey, secretKey)
            .build();
    }
}
