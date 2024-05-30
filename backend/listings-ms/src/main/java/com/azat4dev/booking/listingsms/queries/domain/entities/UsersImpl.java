package com.azat4dev.booking.listingsms.queries.domain.entities;

import com.azat4dev.booking.shared.domain.core.UserId;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public final class UsersImpl implements Users {

    private final UserListingsFactory userListingsFactory;

    @Override
    public User getById(UserId userId) {
        return new UserImpl(
            userId,
            userListingsFactory
        );
    }

    @AllArgsConstructor
    public static class UserImpl implements User {

        private final UserId userId;
        private final UserListingsFactory userListingsFactory;

        @Override
        public UserId getId() {
            return userId;
        }

        @Override
        public UserListings getListings() {
            return this.userListingsFactory.make(userId);
        }
    }
}
