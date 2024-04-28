package com.azat4dev.demobooking.users.domain.interfaces.services;

import com.azat4dev.demobooking.users.domain.services.EmailData;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;

public interface EmailService {
    void send(EmailAddress email, EmailData data);
}