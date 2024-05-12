package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

import com.azat4dev.demobooking.users.users_commands.domain.services.EmailData;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public interface EmailService {
    void send(EmailAddress email, EmailData data);
}