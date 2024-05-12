package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.common.DomainEventsFactory;
import com.azat4dev.demobooking.common.Policy;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.policies.SendVerificationEmailAfterSignUpPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PoliciesConfig {

    @Bean
    Policy<DomainEventNew<UserCreated>> sendVerificationEmailAfterSignUpPolicy(
        DomainEventsBus bus,
        DomainEventsFactory domainEventsFactory
    ) {
        return new SendVerificationEmailAfterSignUpPolicy(
            bus,
            domainEventsFactory
        );
    }
}
