package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;

public interface SendVerificationEmailPolicy {
    void execute(UserCreated event);
}
