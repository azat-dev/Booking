package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingPhoto;
import com.azat4dev.booking.listingsms.generated.server.model.ListingPhotoPathDTO;


public interface MapListingPhotoToDTO {

    ListingPhotoPathDTO map(ListingPhoto listingPhoto);
}
