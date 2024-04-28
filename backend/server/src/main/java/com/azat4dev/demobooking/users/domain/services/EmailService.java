package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.EmailAddress;

public interface EmailService {
    void send(EmailAddress email, EmailData data);
}