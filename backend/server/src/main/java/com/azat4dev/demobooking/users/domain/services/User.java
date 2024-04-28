package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Date;

public final class User {

    private final UserId id;
    private final EmailAddress email;
    private final EmailVerificationStatus emailVerificationStatus;

    private Date createdAt;

    public User(UserId id, EmailAddress email, EmailVerificationStatus emailVerificationStatus, Date createdAt) {
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

        this.id = id;
        this.email = email;
        this.emailVerificationStatus = emailVerificationStatus;
        this.createdAt = createdAt;
    }

    public UserId getId() {
        return id;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public EmailVerificationStatus getEmailVerificationStatus() {
        return emailVerificationStatus;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
}
