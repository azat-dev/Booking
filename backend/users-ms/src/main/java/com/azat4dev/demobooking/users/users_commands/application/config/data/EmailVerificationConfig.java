package com.azat4dev.demobooking.users.users_commands.application.config.data;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.ProvideEmailVerificationToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailVerificationConfig {

    @Bean
    ProvideEmailVerificationToken emailVerificationTokensService() {
        return new ProvideEmailVerificationToken() {
            @Override
            public EmailVerificationToken execute(UserId userId, EmailAddress emailAddress) {
                return new EmailVerificationToken("emailVerificationToken");
            }
        };
    }
}
