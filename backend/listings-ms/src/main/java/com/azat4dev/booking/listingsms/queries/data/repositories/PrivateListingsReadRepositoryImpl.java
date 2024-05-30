package com.azat4dev.booking.listingsms.queries.data.repositories;

import com.azat4dev.booking.listingsms.commands.domain.values.*;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingRecord;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public final class PrivateListingsReadRepositoryImpl implements PrivateListingsReadRepository {

    private final ListingsReadDao dao;
    private final MapRecordToPrivateListingDetails mapper = new MapRecordToPrivateListingDetails();

    @Override
    public Optional<ListingPrivateDetails> findById(ListingId id) {
        return dao.findById(id.getValue())
            .map(mapper);
    }

    public static class MapRecordToPrivateListingDetails implements Function<ListingRecord, ListingPrivateDetails> {
        @Override
        public ListingPrivateDetails apply(ListingRecord record) {
            return new ListingPrivateDetails(
                ListingId.dangerouslyMakeFrom(record.id().toString()),
                OwnerId.checkAndMakeFrom(record.ownerId().toString()),
                ListingTitle.dangerouslyMakeFrom(record.title()),
                ListingStatus.valueOf(record.status()),
                record.description().map(ListingDescription::dangerouslyMakeFrom)
            );
        }
    }
}
