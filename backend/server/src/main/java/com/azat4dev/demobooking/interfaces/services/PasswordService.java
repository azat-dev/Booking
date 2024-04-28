package com.azat4dev.demobooking.interfaces.services;

import com.azat4dev.demobooking.domain.identity.values.Password;

public interface PasswordService {
    EncodedPassword encodePassword(Password password);
}