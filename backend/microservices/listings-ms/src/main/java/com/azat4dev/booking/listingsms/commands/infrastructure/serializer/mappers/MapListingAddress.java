package com.azat4dev.booking.listingsms.commands.infrastructure.serializer.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import com.azat4dev.booking.listingsms.generated.events.dto.AddressDTO;
import com.azat4dev.booking.shared.data.serializers.Mapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class MapListingAddress implements Mapper<ListingAddress, AddressDTO> {

    @Override
    public AddressDTO toDTO(ListingAddress dm) {
        return AddressDTO.builder()
            .country(dm.getCountry().getValue())
            .city(dm.getCity().getValue())
            .street(dm.getStreet().getValue())
            .build();
    }

    @Override
    public ListingAddress toDomain(AddressDTO dto) {
        return ListingAddress.dangerouslyMakeFrom(
            Country.dangerouslyMakeFrom(dto.getCountry()),
            City.dangerouslyMakeFrom(dto.getCity()),
            Street.dangerouslyMakeFrom(dto.getStreet())
        );
    }

    @Override
    public Class<ListingAddress> getDomainClass() {
        return ListingAddress.class;
    }

    @Override
    public Class<AddressDTO> getDTOClass() {
        return AddressDTO.class;
    }
}