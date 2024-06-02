package com.azat4dev.booking.listingsms.commands.application.config.data;


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
public class ListingsPhotosBucketConfig {

    @Bean("listingsPhotoBaseUrl")
    BaseUrl listingsPhotoBaseUrl(
        @Value("${app.objects_storage.bucket.listings-photo.endpoint}")
        URL url
    ) throws BaseUrl.Exception.WrongFormatException {
        return BaseUrl.checkAndMakeFrom(url);
    }

    @Bean("listingsPhotoBucketName")
    BucketName listingsPhotoBucketName(
        @Value("${app.objects_storage.bucket.listings-photo.name}")
        String bucketName
    ) {
        return BucketName.makeWithoutChecks(bucketName);
    }

    @Bean
    @Qualifier("listingsPhotoBucket")
    MediaObjectsBucket minioMediaObjectsBucket(
        @Qualifier("listingsPhotoBaseUrl")
        BaseUrl listingsPhotoBaseUrl,
        @Qualifier("listingsPhotoClient")
        MinioClient minioClient,
        @Qualifier("listingsPhotoBucketName")
        BucketName bucketName
    ) {
        return new MinioMediaObjectsBucket(
            listingsPhotoBaseUrl,
            minioClient,
            bucketName
        );
    }

    @Bean
    @Qualifier("listingsPhotoClient")
    MinioClient minioClient(
        @Qualifier("listingsPhotoBaseUrl") BaseUrl baseUrl,
        @Value("${app.objects_storage.bucket.listings-photo.access-key}") String accessKey,
        @Value("${app.objects_storage.bucket.listings-photo.secret-key}") String secretKey
    ) {
        return MinioClient.builder()
            .endpoint(baseUrl.toString())
            .credentials(accessKey, secretKey)
            .build();
    }
}
