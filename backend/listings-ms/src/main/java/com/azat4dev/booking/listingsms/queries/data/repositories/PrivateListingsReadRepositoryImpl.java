package com.azat4dev.booking.listingsms.queries.data.repositories;

import com.azat4dev.booking.listingsms.commands.domain.values.ListingDescription;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingRecord;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.domain.entities.PrivateListingDetails;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public final class PrivateListingsReadRepositoryImpl implements PrivateListingsReadRepository {

    private final ListingsReadDao dao;
    private final MapRecordToPrivateListingDetails mapper = new MapRecordToPrivateListingDetails();

    @Override
    public Optional<PrivateListingDetails> findById(ListingId id) {
        return dao.findById(id.getValue())
            .map(mapper);
    }

    public static class MapRecordToPrivateListingDetails implements Function<ListingRecord, PrivateListingDetails> {
        @Override
        public PrivateListingDetails apply(ListingRecord record) {
            return new PrivateListingDetails(
                ListingId.dangerouslyMakeFrom(record.id().toString()),
                ListingTitle.dangerouslyMakeFrom(record.title()),
                ListingStatus.valueOf(record.status()),
                record.description().map(ListingDescription::dangerouslyMakeFrom)
            );
        }
    }
}
