package com.azat4dev.demobooking.interfaces.repositories;

import com.azat4dev.demobooking.domain.identity.values.Email;
import com.azat4dev.demobooking.domain.identity.values.UserId;
import com.azat4dev.demobooking.interfaces.services.EncodedPassword;

import java.util.Date;

public final class NewUserData {
    private UserId userId;
    private Date createdAt;
    private Email email;
    private EncodedPassword encodedPassword;

    public NewUserData(
            UserId userId,
            Date createdAt,
            Email email,
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

    public Email getEmail() {
        return email;
    }

    public EncodedPassword getEncodedPassword() {
        return encodedPassword;
    }
}