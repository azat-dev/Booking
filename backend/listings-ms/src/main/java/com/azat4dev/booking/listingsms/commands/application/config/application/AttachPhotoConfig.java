package com.azat4dev.booking.listingsms.commands.application.config.application;

import com.azat4dev.booking.listingsms.commands.application.handlers.GetUrlForUploadListingPhotoHandler;
import com.azat4dev.booking.listingsms.commands.application.handlers.GetUrlForUploadListingPhotoHandlerImpl;
import com.azat4dev.booking.listingsms.commands.domain.handers.GenerateUrlForUploadListingPhoto;
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
}
