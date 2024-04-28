package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.email.EmailBody;

public record EmailData(String subject, EmailBody body) {
}