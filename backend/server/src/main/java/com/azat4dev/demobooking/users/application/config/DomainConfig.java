package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.services.UsersService;
import com.azat4dev.demobooking.users.domain.services.UsersServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public UsersService usersService(
        UsersRepository usersRepository,
        EventsStore eventsStore,
        TimeProvider timeProvider
    ) {
        return new UsersServiceImpl(
            timeProvider,
            usersRepository,
            eventsStore
        );
    }
}
