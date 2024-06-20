package com.azat4dev.booking.listingsms.config.commands.application;

import com.azat4dev.booking.listingsms.config.commands.infrastructure.persistence.files.properties.ListingsPhotoBucketConfigProperties;
import com.azat4dev.booking.listingsms.commands.application.handlers.photo.*;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttachPhotoCommandHandlersConfig {

    @Bean
    GetUrlForUploadListingPhotoHandler getUrlForUploadListingPhotoHandler(
        GenerateUrlForUploadListingPhoto generateUrlForUploadListingPhoto
    ) {
        return new GetUrlForUploadListingPhotoHandlerImpl(
            generateUrlForUploadListingPhoto
        );
    }

    @Bean
    AddNewPhotoToListingHandler addNewPhotoToListingHandler(
        Hosts hosts,
        MakeNewListingPhoto makeNewListingPhoto,
        ListingsCatalog listingsCatalog
    ) {
        return new AddNewPhotoToListingHandlerImpl(
            hosts,
            makeNewListingPhoto,
            listingsCatalog
        );
    }

    @Bean
    MakeNewListingPhoto makeNewListingPhoto(
        ListingsPhotoBucketConfigProperties bucketProperties,
        @Qualifier("listingsPhotoBucket")
        MediaObjectsBucket mediaObjectsBucket
    ) {
        return new MakeNewListingPhotoImpl(
            bucketProperties.getBucketName(),
            mediaObjectsBucket
        );
    }
}
