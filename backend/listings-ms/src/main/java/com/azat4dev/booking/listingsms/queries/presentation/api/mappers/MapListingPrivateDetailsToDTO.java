package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.listingsms.generated.server.model.ListingPrivateDetailsDTO;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;

public interface MapListingPrivateDetailsToDTO {

    ListingPrivateDetailsDTO map(ListingPrivateDetails listingDetails);
}
