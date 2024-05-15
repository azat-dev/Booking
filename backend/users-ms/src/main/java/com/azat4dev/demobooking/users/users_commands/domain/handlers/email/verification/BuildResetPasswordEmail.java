package com.azat4dev.demobooking.users.users_commands.domain.handlers.email.verification;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;

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
