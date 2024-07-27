package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.GuestsCapacity;
import org.jooq.generated.tables.records.ListingsRecord;

@FunctionalInterface
public interface MapGuestsCapacity {

    GuestsCapacity map(ListingsRecord data);
}
