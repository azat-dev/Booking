package com.azat4dev.booking.listingsms.common.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.listingsms.generated.api.bus.dto.listingsms.AddressDTO;
import com.azat4dev.booking.shared.infrastructure.serializers.Serializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapListingAddress implements Serializer<ListingAddress, AddressDTO> {

    @Override
    public AddressDTO serialize(ListingAddress dm) {
        return AddressDTO.builder()
            .country(dm.getCountry().getValue())
            .city(dm.getCity().getValue())
            .street(dm.getStreet().getValue())
            .build();
    }

    @Override
    public ListingAddress deserialize(AddressDTO dto) {
        return ListingAddress.dangerouslyMakeFrom(
            Country.dangerouslyMakeFrom(dto.getCountry()),
            City.dangerouslyMakeFrom(dto.getCity()),
            Street.dangerouslyMakeFrom(dto.getStreet())
        );
    }

    @Override
    public Class<ListingAddress> getOriginalClass() {
        return ListingAddress.class;
    }

    @Override
    public Class<AddressDTO> getSerializedClass() {
        return AddressDTO.class;
    }
}