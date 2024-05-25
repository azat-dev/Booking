package com.azat4dev.booking.users.users_commands.application.config;

import com.azat4dev.booking.shared.domain.core.UserIdFactory;
import com.azat4dev.booking.users.users_commands.application.handlers.CompleteEmailVerificationHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.CompleteEmailVerificationHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.SignUpHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.SignUpHandlerImpl;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.PasswordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandHandlersConfig {

    @Bean
    public SignUpHandler signUpHandler(
        UserIdFactory userIdFactory,
        PasswordService passwordService,
        Users users
    ) {
        return new SignUpHandlerImpl(
            userIdFactory,
            passwordService,
            users
        );
    }

    @Bean
    public CompleteEmailVerificationHandler completeEmailVerificationHandler(VerifyEmailByToken verifyEmailByToken) {
        return new CompleteEmailVerificationHandlerImpl(
            verifyEmailByToken
        );
    }
}
