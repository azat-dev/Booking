package com.azat4dev.booking.listingsms.queries.infrastructure.api.rest.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.generated.server.model.ListingPhotoPathDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapListingPhotoToDTOImpl implements MapListingPhotoToDTO {

    private final GetListingPhotoUrl getListingPhotoUrl;

    @Override
    public ListingPhotoPathDTO map(ListingPhoto listingPhoto) {
        return ListingPhotoPathDTO.builder()
            .photoId(listingPhoto.getId())
            .url(getListingPhotoUrl.execute(listingPhoto.getBucketName(), listingPhoto.getObjectName()))
            .build();
    }
}
