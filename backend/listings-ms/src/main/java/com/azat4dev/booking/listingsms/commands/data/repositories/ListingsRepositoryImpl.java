package com.azat4dev.booking.listingsms.commands.data.repositories;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.data.repositories.mappers.MapListingToRecord;
import com.azat4dev.booking.listingsms.commands.data.repositories.mappers.MapRecordToListing;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public final class ListingsRepositoryImpl implements ListingsRepository {

    private final ListingsDao dao;
    private final TimeProvider timeProvider;
    private final MapListingToRecord mapListingToRecord;
    private final MapRecordToListing mapRecordToListing;

    @Override
    public void addNew(Listing listing) {

        final var data = mapListingToRecord.map(listing);

        try {
            dao.addNew(data);
        } catch (ListingsDao.Exception.ListingAlreadyExists e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Listing> findById(ListingId id) {

        return dao.findById(id.getValue())
            .map(mapRecordToListing::map);
    }
}
