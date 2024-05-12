package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.users.users_commands.data.services.EmailServiceImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${app.mail.host}")
    private String host;

    @Value("${app.mail.protocol}")
    private String protocol;

    @Value("${app.mail.port}")
    private int port;

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl(host, port);
    }
}
