package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;

public record EmailData(
    EmailAddress from,
    String fromName,
    String subject,
    EmailBody body
) {
}