package com.azat4dev.booking.users.users_commands.application.config.presentation;

import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.common.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.booking.users.common.presentation.security.services.CustomUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDetailsConfig {

    @Bean
    public CustomUserDetailsService customUserDetailsService(UsersRepository usersRepository) {
        return new CustomUserDetailsServiceImpl(
            usersRepository
        );
    }
}
