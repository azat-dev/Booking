package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.demobooking.users.users_commands.domain.services.UsersService;
import com.azat4dev.demobooking.users.users_commands.domain.services.UsersServiceImpl;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserIdFactory;
import com.azat4dev.demobooking.users.users_commands.domain.values.UserIdFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DomainConfig {

    @Bean
    public UserIdFactory userIdFactory() {
        return new UserIdFactoryImpl();
    }

    @Bean
    public UsersService usersService(
        TimeProvider timeProvider,
        UnitOfWork unitOfWork
    ) {
        return new UsersServiceImpl(
            timeProvider,
            unitOfWork
        );
    }
}
