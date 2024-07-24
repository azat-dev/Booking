package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.shared.config.domain.AutoConnectPoliciesToBus;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.commands.domain.policies.ProduceSendVerificationEmailCommandAfterSignUpPolicy;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConnectPoliciesToBus
@Configuration
@AllArgsConstructor
public class PoliciesConfig {

    private final DomainEventsBus bus;

    @Bean
    ProduceSendVerificationEmailCommandAfterSignUpPolicy sendVerificationEmailAfterSignUpPolicy() {
        return new ProduceSendVerificationEmailCommandAfterSignUpPolicy(bus);
    }
}
