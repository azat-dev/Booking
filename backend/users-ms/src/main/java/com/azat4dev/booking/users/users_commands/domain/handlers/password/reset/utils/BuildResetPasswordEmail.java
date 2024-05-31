package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailBody;

@FunctionalInterface
public interface BuildResetPasswordEmail {

    EmailData execute(UserId userId, EmailAddress email);

    record EmailData(
        String fromName,
        EmailAddress fromAddress,
        String subject,
        EmailBody body
    ) {
    }
}
