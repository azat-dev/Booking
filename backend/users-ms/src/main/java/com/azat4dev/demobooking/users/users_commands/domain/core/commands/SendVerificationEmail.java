package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.DomainEventPayload;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record SendVerificationEmail(
    UserId userId,
    EmailAddress email,
    FullName fullName
) implements DomainEventPayload {
}
