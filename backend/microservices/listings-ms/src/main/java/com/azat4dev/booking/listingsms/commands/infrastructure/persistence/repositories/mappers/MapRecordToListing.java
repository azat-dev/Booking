package com.azat4dev.booking.listingsms.commands.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import org.jooq.generated.tables.records.ListingsRecord;

public interface MapRecordToListing {
    Listing map(ListingsRecord data);
}
