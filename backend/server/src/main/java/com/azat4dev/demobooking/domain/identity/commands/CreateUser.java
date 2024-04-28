package com.azat4dev.demobooking.domain.identity.commands;

import com.azat4dev.demobooking.domain.common.CommandId;
import com.azat4dev.demobooking.domain.identity.values.UserId;

public final class CreateUser {

    private CommandId id;
    private String email;
    private String password;
    private UserId userId;

    public CreateUser(
            CommandId id,
            UserId userId,
            String email,
            String password
    ) {
        if (id == null) {
            throw new NullPointerException("id");
        }

        if (email == null) {
            throw new NullPointerException("email");
        }

        if (password == null) {
            throw new NullPointerException("password");
        }

        this.id = id;
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public CommandId getId() {
        return id;
    }

    public UserId getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
