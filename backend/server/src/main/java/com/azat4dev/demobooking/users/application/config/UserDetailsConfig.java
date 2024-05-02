package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsServiceImpl;
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
