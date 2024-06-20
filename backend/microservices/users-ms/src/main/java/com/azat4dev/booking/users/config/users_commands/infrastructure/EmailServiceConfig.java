package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.users.config.users_commands.properties.EmailServerConfigProperties;
import com.azat4dev.booking.users.commands.infrastructure.services.email.EmailServiceImpl;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class EmailServiceConfig {

    private final EmailServerConfigProperties emailServerConfig;

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(emailServerConfig.getHost(), emailServerConfig.getPort());
    }
}
