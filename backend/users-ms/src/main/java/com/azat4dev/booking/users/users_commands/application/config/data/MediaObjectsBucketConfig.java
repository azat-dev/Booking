package com.azat4dev.booking.users.users_commands.application.config.data;


import com.azat4dev.booking.shared.data.repositories.files.MinioMediaObjectsBucket;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.BaseUrl;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class MediaObjectsBucketConfig {

    @Bean("usersPhotoBaseUrl")
    BaseUrl usersPhotoBaseUrl(
        @Value("${app.objects_storage.bucket.users-photo.endpoint}")
        URL url
    ) throws BaseUrl.Exception.WrongFormatException {
        return BaseUrl.checkAndMakeFrom(url);
    }

    @Bean("usersPhotoBucketName")
    BucketName usersPhotoBucketName(
        @Value("${app.objects_storage.bucket.users-photo.name}")
        String bucketName
    ) {
        return BucketName.makeWithoutChecks(bucketName);
    }

    @Bean
    @Qualifier("usersPhotoBucket")
    MediaObjectsBucket minioMediaObjectsBucket(
        @Qualifier("usersPhotoBaseUrl")
        BaseUrl usersPhotoBaseUrl,
        @Qualifier("usersPhotoClient")
        MinioClient minioClient,
        @Qualifier("usersPhotoBucketName")
        BucketName bucketName
    ) {
        return new MinioMediaObjectsBucket(
            usersPhotoBaseUrl,
            minioClient,
            bucketName
        );
    }

    @Bean
    @Qualifier("usersPhotoClient")
    MinioClient minioClient(
        @Qualifier("usersPhotoBaseUrl") BaseUrl baseUrl,
        @Value("${app.objects_storage.bucket.users-photo.access-key}") String accessKey,
        @Value("${app.objects_storage.bucket.users-photo.secret-key}") String secretKey
    ) {
        return MinioClient.builder()
            .endpoint(baseUrl.toString())
            .credentials(accessKey, secretKey)
            .build();
    }
}
