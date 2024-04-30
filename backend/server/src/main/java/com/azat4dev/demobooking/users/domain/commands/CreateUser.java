package com.azat4dev.demobooking.users.domain.commands;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;

public record CreateUser(
    CommandId id,
    UserId userId,
    FullName fullName,
    EmailAddress email,
    EncodedPassword encodedPassword
) {
}
