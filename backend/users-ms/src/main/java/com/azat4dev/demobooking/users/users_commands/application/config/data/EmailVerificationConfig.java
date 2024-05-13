package com.azat4dev.demobooking.users.users_commands.application.config.data;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokensService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailVerificationConfig {

    @Bean
    EmailVerificationTokensService emailVerificationTokensService() {
        return new EmailVerificationTokensService() {
            @Override
            public EmailVerificationToken generateFor(UserId userId, EmailAddress emailAddress) {
                return new EmailVerificationToken("emailVerificationToken");
            }

            @Override
            public void verify(EmailVerificationToken token) throws Exception {

            }
        };
    }
}
