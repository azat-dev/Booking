package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

import com.azat4dev.demobooking.users.users_commands.domain.values.Password;

public interface PasswordService {
    EncodedPassword encodePassword(Password password);
}