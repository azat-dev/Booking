package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.listingsms.queries.domain.interfaces.PrivateListingsReadRepository;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class UserListingsFactoryImpl implements UserListingsFactory {

    private final PrivateListingsReadRepository listingsRepository;

    @Override
    public UserListings make(UserId userId) {
        return new UserListingsImpl(userId, listingsRepository);
    }
}
