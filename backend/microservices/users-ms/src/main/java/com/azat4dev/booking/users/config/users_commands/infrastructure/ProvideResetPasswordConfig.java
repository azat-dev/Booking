package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.config.users_commands.properties.ResetPasswordConfigProperties;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils.ProvideResetPasswordToken;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils.ProvideResetPasswordTokenImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class ProvideResetPasswordConfig {

    private final ResetPasswordConfigProperties resetPasswordConfig;

    @Bean
    ProvideResetPasswordToken generateResetPasswordToken(
        JwtDataEncoder jwtDataEncoder,
        TimeProvider timeProvider
    ) {
        return new ProvideResetPasswordTokenImpl(
            resetPasswordConfig.getTokenExpiresIn(),
            jwtDataEncoder,
            timeProvider
        );
    }
}
