package com.azat4dev.demobooking.users.users_queries.application.config;

import com.azat4dev.demobooking.users.users_queries.domain.interfaces.repositories.UsersReadRepository;
import com.azat4dev.demobooking.users.users_queries.domain.services.UsersQueryService;
import com.azat4dev.demobooking.users.users_queries.domain.services.UsersQueryServiceImpl;
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
