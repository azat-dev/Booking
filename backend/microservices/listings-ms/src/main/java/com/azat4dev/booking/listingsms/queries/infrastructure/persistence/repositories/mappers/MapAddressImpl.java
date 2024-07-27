package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.address.City;
import com.azat4dev.booking.listingsms.common.domain.values.address.Country;
import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import com.azat4dev.booking.listingsms.common.domain.values.address.Street;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Optional;

public final class MapAddressImpl implements MapAddress {
    @Override
    public Optional<ListingAddress> map(ListingsRecord data) {

        if (data.getAddressCountry() == null) {
            return Optional.empty();
        }

        return Optional.of(
            ListingAddress.dangerouslyMakeFrom(
                Country.dangerouslyMakeFrom(data.getAddressCountry()),
                City.dangerouslyMakeFrom(data.getAddressCity()),
                Street.dangerouslyMakeFrom(data.getAddressStreet())
            )
        );
    }
}
