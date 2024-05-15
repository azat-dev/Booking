package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;

public interface EmailService {
    void send(EmailAddress email, EmailData data);

    record EmailData(
        EmailAddress from,
        String fromName,
        String subject,
        EmailBody body
    ) {
    }
}