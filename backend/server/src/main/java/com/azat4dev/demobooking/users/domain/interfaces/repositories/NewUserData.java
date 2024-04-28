package com.azat4dev.demobooking.users.domain.interfaces.repositories;

import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;

import java.util.Date;

public final class NewUserData {
    private final UserId userId;
    private final Date createdAt;
    private final EmailAddress email;
    private final EncodedPassword encodedPassword;

    public NewUserData(
            UserId userId,
            Date createdAt,
            EmailAddress email,
            EncodedPassword encodedPassword
    ) {

        if (userId == null) {
            throw new NullPointerException("userId");
        }

        if (createdAt == null) {
            throw new NullPointerException("createdAt");
        }

        if (email == null) {
            throw new NullPointerException("email");
        }

        if (encodedPassword == null) {
            throw new NullPointerException("encodedPassword");
        }

        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.encodedPassword = encodedPassword;
    }

    public UserId getUserId() {
        return userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public EncodedPassword getEncodedPassword() {
        return encodedPassword;
    }
}