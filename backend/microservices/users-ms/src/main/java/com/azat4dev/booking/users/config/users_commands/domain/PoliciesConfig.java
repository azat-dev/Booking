package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.common.domain.ConnectPoliciesConfig;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.commands.domain.policies.ProduceSendVerificationEmailCommandAfterSignUpPolicy;
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
