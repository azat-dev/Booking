package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.values.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class DataConfig {

    @Bean
    UsersRepository usersRepository() {
        return new UsersRepository() {
            @Override
            public void createUser(NewUserData newUserData) {

            }

            @Override
            public Optional<User> findById(UserId id) {
                return Optional.empty();
            }
        };
    }

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    EventsStore eventsStore() {
        return null;
    }
}
