package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.interfaces.repositories.ListingsRepository;
import com.azat4dev.booking.listingsms.commands.domain.values.HostId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class HostListingsFactoryImpl implements HostListingsFactory {

    private final ListingsRepository listingsRepository;

    @Override
    public HostListings make(HostId hostId) {
        return new HostListingsImpl(hostId, listingsRepository);
    }
}
