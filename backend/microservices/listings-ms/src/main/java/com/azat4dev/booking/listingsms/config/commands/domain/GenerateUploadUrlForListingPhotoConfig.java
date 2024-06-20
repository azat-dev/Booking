package com.azat4dev.booking.listingsms.config.commands.domain;

import com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.files.properties.ListingsPhotoBucketConfigProperties;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateObjectNameForListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateObjectNameForListingPhotoImpl;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhotoImpl;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateUploadUrlForListingPhotoConfig {

    @Bean
    public GenerateObjectNameForListingPhoto generateObjectNameForListingPhoto(TimeProvider timeProvider) {
        return new GenerateObjectNameForListingPhotoImpl(timeProvider);
    }

    @Bean
    public GenerateUrlForUploadListingPhoto generateUrlForUploadListingPhoto(
        ListingsPhotoBucketConfigProperties bucketProperties,
        @Qualifier("listingsPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        GenerateObjectNameForListingPhoto generateObjectName,
        DomainEventsBus bus
    ) {
        return new GenerateUrlForUploadListingPhotoImpl(
            bucketProperties.getUploadUrlExpiresIn(),
            generateObjectName,
            userPhotoBucket,
            bus
        );
    }
}
