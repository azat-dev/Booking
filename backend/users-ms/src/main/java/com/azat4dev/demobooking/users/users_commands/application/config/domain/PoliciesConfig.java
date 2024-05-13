package com.azat4dev.demobooking.users.users_commands.application.config.domain;

import com.azat4dev.demobooking.common.domain.ConnectPoliciesConfig;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.DomainEventsFactory;
import com.azat4dev.demobooking.users.users_commands.domain.policies.ProduceSendVerificationEmailCommandAfterSignUpPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ConnectPoliciesConfig.class)
@Configuration
public class PoliciesConfig {

    @Bean
    ProduceSendVerificationEmailCommandAfterSignUpPolicy sendVerificationEmailAfterSignUpPolicy(
        DomainEventsBus bus,
        DomainEventsFactory domainEventsFactory
    ) {
        return new ProduceSendVerificationEmailCommandAfterSignUpPolicy(
            bus,
            domainEventsFactory
        );
    }
}