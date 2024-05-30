package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.shared.domain.core.UserId;

public interface User {

    UserId getId();

    UserListings getListings();
}
