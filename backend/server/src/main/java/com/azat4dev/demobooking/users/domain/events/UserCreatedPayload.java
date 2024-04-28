package com.azat4dev.demobooking.users.domain.events;

import com.azat4dev.demobooking.users.domain.services.User;

public final class UserCreatedPayload {

    private User user;

    public UserCreatedPayload(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
