package com.azat4dev.booking.listingsms.queries.infrastructure.persistence.repositories.mappers;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPublicDetails;
import org.jooq.generated.tables.records.ListingsRecord;

public interface MapRecordToListingPublicDetails {

    ListingPublicDetails map(ListingsRecord record) throws Exception.ListingNotPublished;

    abstract class Exception extends RuntimeException {
        protected Exception(String message) {
            super(message);
        }

        public static class ListingNotPublished extends Exception {
            public ListingNotPublished(ListingId id) {
                super("Listing with id " + id + " is not published");
            }
        }
    }
}
