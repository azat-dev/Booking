package com.azat4dev.booking.listingsms.commands.data.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import org.jooq.generated.tables.records.ListingsRecord;

public interface MapRecordToListing {
    Listing map(ListingsRecord data);
}
