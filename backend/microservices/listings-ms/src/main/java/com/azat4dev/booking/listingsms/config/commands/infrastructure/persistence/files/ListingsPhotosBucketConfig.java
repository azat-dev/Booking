package com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.files;


import com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.files.properties.ListingsPhotoBucketConfigProperties;
import com.azat4dev.booking.shared.infrastructure.persistence.repositories.files.MinioMediaObjectsBucket;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({ListingsPhotoBucketConfigProperties.class})
@Configuration
public class ListingsPhotosBucketConfig {

    @Bean
    @Qualifier("listingsPhotoBucket")
    MediaObjectsBucket minioMediaObjectsBucket(
        ListingsPhotoBucketConfigProperties bucketProperties,
        @Qualifier("listingsPhotoClient")
        MinioClient minioClient
    ) {
        return new MinioMediaObjectsBucket(
            bucketProperties.getBaseUrl(),
            minioClient,
            bucketProperties.getBucketName()
        );
    }

    @Bean
    @Qualifier("listingsPhotoClient")
    MinioClient minioClient(ListingsPhotoBucketConfigProperties bucketProperties) {
        return MinioClient.builder()
            .endpoint(bucketProperties.getBaseUrl().toString())
            .credentials(bucketProperties.getAccessKey(),  bucketProperties.getSecretKey())
            .build();
    }
}
