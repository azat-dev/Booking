package com.azat4dev.booking.listingsms.commands.domain.entities;

import com.azat4dev.booking.listingsms.commands.domain.values.HostId;

public interface Host {

    HostId getId();

    HostListings getListings();
}
