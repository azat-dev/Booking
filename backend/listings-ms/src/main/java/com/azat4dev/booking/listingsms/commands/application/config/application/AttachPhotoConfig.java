package com.azat4dev.booking.listingsms.commands.application.config.application;

import com.azat4dev.booking.listingsms.commands.application.handlers.photo.*;
import com.azat4dev.booking.listingsms.commands.domain.entities.Hosts;
import com.azat4dev.booking.listingsms.commands.domain.entities.ListingsCatalog;
import com.azat4dev.booking.listingsms.commands.domain.handers.photo.GenerateUrlForUploadListingPhoto;
import com.azat4dev.booking.shared.domain.interfaces.files.MediaObjectsBucket;
import com.azat4dev.booking.shared.domain.values.files.BucketName;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AttachPhotoConfig {

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
        @Value("listingsPhotoBucketName")
        String listingsPhotoBucketName,
        @Qualifier("listingsPhotoBucket")
        MediaObjectsBucket mediaObjectsBucket
    ) throws BucketName.Exception {
        return new MakeNewListingPhotoImpl(
            BucketName.checkAndMake(listingsPhotoBucketName),
            mediaObjectsBucket
        );
    }
}
