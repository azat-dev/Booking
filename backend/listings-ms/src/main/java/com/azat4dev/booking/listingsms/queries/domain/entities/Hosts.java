package com.azat4dev.booking.listingsms.queries.domain.entities;


import com.azat4dev.booking.listingsms.commands.domain.values.HostId;

public interface Hosts {

    Host getById(HostId hostId);
}
