package com.azat4dev.demobooking.users.users_commands.domain.commands;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;

public final class SendVerificationEmailPayload {
    private final UserId userId;
    private final EmailAddress email;

    public SendVerificationEmailPayload(
        UserId userId,
        EmailAddress email
    ) {
        this.userId = userId;
        this.email = email;
    }
}
