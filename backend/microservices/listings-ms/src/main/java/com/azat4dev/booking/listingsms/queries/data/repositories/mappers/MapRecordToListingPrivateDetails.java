package com.azat4dev.booking.listingsms.queries.data.repositories.mappers;

import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import org.jooq.generated.tables.records.ListingsRecord;

public interface MapRecordToListingPrivateDetails {

    ListingPrivateDetails map(ListingsRecord record);
}
