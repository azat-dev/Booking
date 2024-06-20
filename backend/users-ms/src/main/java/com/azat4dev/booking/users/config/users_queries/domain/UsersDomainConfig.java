package com.azat4dev.booking.users.config.users_queries.domain;

import com.azat4dev.booking.users.queries.domain.interfaces.repositories.UsersReadRepository;
import com.azat4dev.booking.users.queries.domain.services.UsersQueryService;
import com.azat4dev.booking.users.queries.domain.services.UsersQueryServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("usersQueriesDomainConfig")
public class UsersDomainConfig {

    @Bean
    public UsersQueryService usersQueryService(UsersReadRepository usersReadRepository) {
        return new UsersQueryServiceImpl(
            usersReadRepository
        );
    }
}
