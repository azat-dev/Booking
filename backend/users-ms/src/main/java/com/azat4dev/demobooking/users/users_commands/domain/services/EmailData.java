package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;

public record EmailData(String subject, EmailBody body) {
}