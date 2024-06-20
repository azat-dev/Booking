package com.azat4dev.booking.users.commands.infrastructure.services.password;

import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.Password;
import com.azat4dev.booking.users.commands.domain.interfaces.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public final class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;

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
