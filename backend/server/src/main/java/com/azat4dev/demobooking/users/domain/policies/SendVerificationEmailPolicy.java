package com.azat4dev.demobooking.users.domain.policies;

import com.azat4dev.demobooking.users.domain.events.UserCreated;

public interface SendVerificationEmailPolicy {
    void execute(UserCreated event);
}
