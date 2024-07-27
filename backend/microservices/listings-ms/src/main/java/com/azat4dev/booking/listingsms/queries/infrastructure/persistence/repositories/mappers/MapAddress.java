package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.common.domain.values.address.ListingAddress;
import org.jooq.generated.tables.records.ListingsRecord;

import java.util.Optional;

@FunctionalInterface
public interface MapAddress {

    Optional<ListingAddress> map(ListingsRecord data);
}
