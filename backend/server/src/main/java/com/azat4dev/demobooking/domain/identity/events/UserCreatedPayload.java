package com.azat4dev.demobooking.domain.identity.events;

import com.azat4dev.demobooking.domain.identity.User;

public final class UserCreatedPayload {

    private User user;

    public UserCreatedPayload(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
