package com.azat4dev.demobooking.users.users_commands.application.config.domain;

import com.azat4dev.demobooking.common.*;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.SendVerificationEmailHandler;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokensService;
import com.azat4dev.demobooking.users.users_commands.domain.policies.ProduceSendVerificationEmailCommandAfterSignUpPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.URL;

@Import(ConnectPoliciesConfig.class)
@Configuration
public class PoliciesConfig {

    @Bean
    Policy<DomainEventNew<UserCreated>> sendVerificationEmailAfterSignUpPolicy(
        DomainEventsBus bus,
        DomainEventsFactory domainEventsFactory
    ) {
        return new ProduceSendVerificationEmailCommandAfterSignUpPolicy(
            bus,
            domainEventsFactory
        );
    }

    @Bean
    Policy<DomainEventNew<SendVerificationEmail>> sendVerificationEmailPolicy(
        @Value
        ("${app.verification_email.base_verification_link_url}")
        URL baseVerificationLinkUrl,
        @Value("${app.verification_email.outgoing.fromAddress}")
        String fromAddress,
        @Value("${app.verification_email.outgoing.fromName}")
        String fromName,
        EmailService emailService,
        EmailVerificationTokensService emailVerificationTokensService
    ) {
        return new SendVerificationEmailHandler(
            baseVerificationLinkUrl,
            EmailAddress.checkAndMakeFromString(fromAddress),
            fromName,
            emailService,
            emailVerificationTokensService
        );
    }
}
