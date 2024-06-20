package com.azat4dev.booking.users.config.users_commands.presentation;

import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.CustomUserDetailsServiceImpl;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
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
