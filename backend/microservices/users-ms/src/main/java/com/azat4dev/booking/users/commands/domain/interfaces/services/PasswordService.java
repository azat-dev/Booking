package com.azat4dev.booking.users.commands.domain.interfaces.services;

import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.Password;

public interface PasswordService {
    EncodedPassword encodePassword(Password password);

    boolean matches(Password password, EncodedPassword encodedPassword);
}