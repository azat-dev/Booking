package com.azat4dev.demobooking.users.users_commands.application.config.domain;

import com.azat4dev.demobooking.common.domain.annotations.CommandHandlerBean;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.*;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ResetPasswordConfig {

    private final EmailService emailService;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;

    @CommandHandlerBean
    public ResetPasswordByEmailHandler resetPasswordByEmailHandler(BuildResetPasswordEmail buildResetPasswordEmail) {
        return new ResetPasswordByEmailHandler(
            usersRepository,
            buildResetPasswordEmail,
            emailService,
            bus
        );
    }

    @Bean
    public BuildResetPasswordEmail buildResetPasswordEmail(
        @Value("${app.reset_password.outgoing_email.fromName}") String fromName,
        @Value("${app.reset_password.outgoing_email.fromAddress}") String fromAddress,
        @Value("${app.reset_password.outgoing_email.subject}") String subject,
        GenerateResetPasswordLink generateResetPasswordLink
    ) {
        return new BuildResetPasswordEmailImpl(
            fromName,
            EmailAddress.checkAndMakeFromString(fromAddress),
            subject,
            generateResetPasswordLink
        );
    }

    @Bean
    public GenerateResetPasswordLink generateResetPasswordLink() {
        return new GenerateResetPasswordLink() {
            @Override
            public ResetPasswordLink execute(UserId userId) {
                return new ResetPasswordLink("http://localhost:8080/reset-password/" + userId.value());
            }
        };
    }
}
