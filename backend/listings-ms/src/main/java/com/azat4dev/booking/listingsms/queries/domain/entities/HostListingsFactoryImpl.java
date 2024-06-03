package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class HostListingsFactoryImpl implements HostListingsFactory {

    private final PrivateListingsReadRepository listingsRepository;

    @Override
    public HostListings make(HostId hostId) {
        return new HostListingsImpl(hostId, listingsRepository);
    }
}
