package com.azat4dev.demobooking.domain.identity;

import com.azat4dev.demobooking.domain.identity.values.Email;
import com.azat4dev.demobooking.domain.identity.values.UserId;

import java.util.Date;

public final class User {

    private final UserId id;
    private final Email email;
    private final EmailVerificationStatus emailVerificationStatus;

    private Date createdAt;

    public User(UserId id, Email email, EmailVerificationStatus emailVerificationStatus, Date createdAt) {
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

    public Email getEmail() {
        return email;
    }

    public EmailVerificationStatus getEmailVerificationStatus() {
        return emailVerificationStatus;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
}
