package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.token.TokenService;

@Configuration
public class DataConfig {

    @Bean
    UsersRepository usersRepository() {
        return null;
    }

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    TokenService tokenService() {
        return null;
    }

    @Bean
    EmailService emailService() {
        return null;
    }

    @Bean
    PasswordService passwordService() {
        return null;
    }

    @Bean
    EventsStore eventsStore() {
        return null;
    }
}
