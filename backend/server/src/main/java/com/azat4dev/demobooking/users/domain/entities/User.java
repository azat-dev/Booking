package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Date;

public record User(UserId id, EmailAddress email, EmailVerificationStatus emailVerificationStatus, Date createdAt) {

    public User {
        if (id == null) {
            throw new NullPointerException("id");
        }

        if (email == null) {
            throw new NullPointerException("email");
        }

        if (emailVerificationStatus == null) {
            throw new NullPointerException("emailVerificationStatus");
        }

        if (createdAt == null) {
            throw new NullPointerException("createdAt");
        }

    }
}
