package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import org.jooq.generated.tables.records.ListingsRecord;

public class MapGuestsCapacityImpl implements MapGuestsCapacity {
    @Override
    public GuestsCapacity map(ListingsRecord data) {
        return GuestsCapacity.dangerouslyMake(
            data.getGuestsCapacityAdults(),
            data.getGuestsCapacityChildren(),
            data.getGuestsCapacityInfants()
        );
    }
}
