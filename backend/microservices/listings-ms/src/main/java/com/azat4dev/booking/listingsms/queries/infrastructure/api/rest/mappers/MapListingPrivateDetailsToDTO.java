package com.azat4dev.booking.listingsms.queries.infrastructure.api.rest.mappers;

import com.azat4dev.booking.listingsms.generated.server.model.ListingPrivateDetailsDTO;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;

public interface MapListingPrivateDetailsToDTO {

    ListingPrivateDetailsDTO map(ListingPrivateDetails listingDetails);
}
