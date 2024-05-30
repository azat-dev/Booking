package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.generated.server.model.ListingPrivateDetailsDTO;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;

public final class MapListingPrivateDetailsToDTOImpl implements MapListingPrivateDetailsToDTO {
    @Override
    public ListingPrivateDetailsDTO map(ListingPrivateDetails listingDetails) {
        return new ListingPrivateDetailsDTO(
            listingDetails.id().getValue(),
            listingDetails.title().getValue(),
            listingDetails.description()
                .map(ListingDescription::getValue)
                .orElse(null)
        );
    }
}
