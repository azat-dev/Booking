package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.shared.domain.core.UserId;

public interface UserListingsFactory {

    UserListings make(UserId userId);
}
