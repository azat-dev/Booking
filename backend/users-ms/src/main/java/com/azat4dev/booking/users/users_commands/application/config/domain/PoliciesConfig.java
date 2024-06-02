package com.azat4dev.booking.users.users_commands.application.config.domain;

import com.azat4dev.booking.common.domain.ConnectPoliciesConfig;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.users_commands.domain.policies.ProduceSendVerificationEmailCommandAfterSignUpPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ConnectPoliciesConfig.class)
@Configuration
public class PoliciesConfig {

    @Bean
    ProduceSendVerificationEmailCommandAfterSignUpPolicy sendVerificationEmailAfterSignUpPolicy(DomainEventsBus bus) {
        return new ProduceSendVerificationEmailCommandAfterSignUpPolicy(bus);
    }
}
