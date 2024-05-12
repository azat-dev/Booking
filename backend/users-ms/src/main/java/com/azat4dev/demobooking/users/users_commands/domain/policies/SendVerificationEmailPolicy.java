package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;

public interface SendVerificationEmailPolicy {

    void execute(DomainEventNew<UserCreated> event);
}
