package com.azat4dev.booking.listingsms.commands.data.repositories;

import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingData;
import com.azat4dev.booking.listingsms.commands.data.dao.listings.ListingsDao;
import com.azat4dev.booking.listingsms.commands.domain.entities.Listing;
import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingStatus;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingTitle;
import com.azat4dev.booking.listingsms.commands.domain.values.OwnerId;
import com.azat4dev.booking.shared.utils.TimeProvider;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public final class ListingsRepositoryImpl implements ListingsRepository {

    private final ListingsDao dao;
    private final TimeProvider timeProvider;

    @Override
    public void addNew(Listing listing) {

        final var now = timeProvider.currentTime();

        final var data = new ListingData(
            listing.getId().getValue(),
            now,
            now,
            listing.getOwnerId().getValue(),
            listing.getTitle().getValue(),
            listing.getStatus().name(),
            Optional.empty()
        );

        try {
            dao.addNew(data);
        } catch (ListingsDao.Exception.ListingAlreadyExists e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Listing> findById(ListingId id) {

        return dao.findById(id.getValue())
            .map(data -> Listing.internalMake(
                ListingId.dangerouslyMakeFrom(data.id().toString()),
                ListingStatus.valueOf(data.status()),
                data.createdAt(),
                data.updatedAt(),
                OwnerId.dangerouslyMakeFrom(data.ownerId().toString()),
                ListingTitle.dangerouslyMakeFrom(data.title()),
                Optional.empty(),
                Optional.empty()
            ));
    }
}
