package com.azat4dev.booking.listingsms.queries.presentation.api.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.generated.server.model.*;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;

public final class MapListingPrivateDetailsToDTOImpl implements MapListingPrivateDetailsToDTO {

    private GuestsCapacityDTO mapGuestsCapacity(GuestsCapacity guestsCapacity) {
        return GuestsCapacityDTO.builder()
            .adults(guestsCapacity.getAdults())
            .children(guestsCapacity.getChildren())
            .infants(guestsCapacity.getInfants())
            .build();
    }

    private AddressDTO mapAddress(ListingAddress address) {
        return AddressDTO.builder()
            .country(address.getCountry().getValue())
            .city(address.getCity().getValue())
            .street(address.getStreet().getValue())
            .build();
    }

    @Override
    public ListingPrivateDetailsDTO map(ListingPrivateDetails listingDetails) {
        return ListingPrivateDetailsDTO.builder()
            .id(listingDetails.id().getValue())
            .title(listingDetails.title().getValue())
            .description(
                listingDetails.description()
                    .map(ListingDescription::getValue)
                    .orElse(null)

            ).guestCapacity(
                mapGuestsCapacity(listingDetails.guestsCapacity())
            ).propertyType(
                listingDetails.propertyType()
                    .map(p -> PropertyTypeDTO.fromValue(p.name()))
                    .orElse(null)
            ).roomType(
                listingDetails.roomType()
                    .map(r -> RoomTypeDTO.fromValue(r.name()))
                    .orElse(null)
            ).address(
                listingDetails.address().map(this::mapAddress)
                    .orElse(null)
            ).build();
    }
}
