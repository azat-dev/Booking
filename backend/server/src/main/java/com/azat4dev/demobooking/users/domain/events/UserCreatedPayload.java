package com.azat4dev.demobooking.users.domain.events;

import com.azat4dev.demobooking.users.domain.entities.User;

public record UserCreatedPayload(User user) {

}
