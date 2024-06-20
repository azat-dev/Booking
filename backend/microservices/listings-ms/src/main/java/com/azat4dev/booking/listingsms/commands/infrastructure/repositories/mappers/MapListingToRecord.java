package com.azat4dev.booking.listingsms.commands.infrastructure.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import org.jooq.generated.tables.records.ListingsRecord;

public interface MapListingToRecord {

    ListingsRecord map(Listing listing);
}
