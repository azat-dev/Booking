package com.azat4dev.booking.listingsms.queries.data.repositories;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.commands.domain.values.ListingId;
import com.azat4dev.booking.listingsms.queries.data.dao.ListingsReadDao;
import com.azat4dev.booking.listingsms.queries.data.repositories.mappers.MapRecordToListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.entities.ListingPrivateDetails;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public final class PrivateListingsReadRepositoryImpl implements PrivateListingsReadRepository {

    private final ListingsReadDao dao;
    private final MapRecordToListingPrivateDetails mapper;

    @Override
    public Optional<ListingPrivateDetails> findById(ListingId id) {
        return dao.findById(id.getValue())
            .map(mapper::map);
    }

    @Override
    public List<ListingPrivateDetails> findAllByHostId(HostId hostId) {
        return dao.findAllByHostId(hostId.getValue())
            .stream()
            .map(mapper::map)
            .toList();
    }
}
