package com.azat4dev.demobooking.users.domain.interfaces.services;

import com.azat4dev.demobooking.users.domain.values.Password;

public interface PasswordService {
    EncodedPassword encodePassword(Password password);
}