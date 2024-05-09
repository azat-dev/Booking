package com.azat4dev.demobooking.users.users_commands.data.services;

import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.users_commands.domain.values.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EncodedPassword encodePassword(Password password) {
        return new EncodedPassword(
            this.passwordEncoder.encode(password.getValue())
        );
    }

    @Override
    public boolean matches(Password password, EncodedPassword encodedPassword) {
        return this.passwordEncoder.matches(password.getValue(), encodedPassword.value());
    }
}
