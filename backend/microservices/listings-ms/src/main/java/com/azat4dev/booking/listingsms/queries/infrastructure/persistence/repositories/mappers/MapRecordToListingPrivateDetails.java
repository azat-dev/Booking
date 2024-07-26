package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import org.jooq.generated.tables.records.ListingsRecord;

public interface MapRecordToListingPrivateDetails {

    ListingPrivateDetails map(ListingsRecord record);
}
