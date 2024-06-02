package com.azat4dev.booking.listingsms.commands.application.config.domain;

import com.azat4dev.booking.listingsms.commands.domain.handers.GenerateObjectNameForListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.GenerateObjectNameForListingPhotoImpl;
import com.azat4dev.booking.listingsms.commands.domain.handers.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.listingsms.commands.domain.handers.GenerateUrlForUploadListingPhotoImpl;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        @Value("${app.objects_storage.bucket.listings-photo.upload-url.expires-in-seconds}")
        int expireInSeconds,
        @Qualifier("listingsPhotoBucket")
        MediaObjectsBucket userPhotoBucket,
        GenerateObjectNameForListingPhoto generateObjectName,
        DomainEventsBus bus
    ) {
        return new GenerateUrlForUploadListingPhotoImpl(
            expireInSeconds,
            generateObjectName,
            userPhotoBucket,
            bus
        );
    }
}
