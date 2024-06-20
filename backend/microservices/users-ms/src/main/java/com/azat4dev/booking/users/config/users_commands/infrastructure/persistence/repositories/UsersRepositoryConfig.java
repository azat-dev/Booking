package com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.*;
import com.azat4dev.booking.users.commands.infrastructure.persistence.dao.UsersDao;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserDataToDomain;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserDataToDomainImpl;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserToData;
import com.azat4dev.booking.users.commands.infrastructure.persistence.repositories.mappers.MapUserToDataImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersRepositoryConfig {

    @Bean
    MapUserToData mapNewUserToData() {
        return new MapUserToDataImpl();
    }

    @Bean
    MapUserDataToDomain mapUserDataToDomain() {
        return new MapUserDataToDomainImpl();
    }

    @Bean
    UsersRepository usersRepository(
        MapUserToData mapUserToData,
        MapUserDataToDomain mapUserDataToDomain,
        UsersDao usersDao
    ) {
        return new UsersRepositoryImpl(
            mapUserToData,
            mapUserDataToDomain,
            usersDao
        );
    }
}
