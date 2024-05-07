package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.data.repositories.*;
import com.azat4dev.demobooking.users.data.repositories.jpa.JpaUsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.domain.services.EmailData;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    UsersRepository usersRepository(
        MapNewUserToData mapNewUserToData,
        MapUserDataToDomain mapUserDataToDomain,
        JpaUsersRepository jpaUsersRepository
    ) {
        return new UsersRepositoryImpl(
            mapNewUserToData,
            mapUserDataToDomain,
            jpaUsersRepository
        );
    }

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    EventsStore eventsStore() {
        return new EventsStore() {
            @Override
            public void publish(DomainEvent event) {

            }

            @Override
            public void close() {

            }
        };
    }

    @Bean
    EmailService emailService() {
        return new EmailService() {
            @Override
            public void send(EmailAddress email, EmailData data) {
                System.out.println("Email sent to " + email);
            }
        };
    }

    @Bean
    MapNewUserToData mapNewUserToData() {
        return new MapNewUserToDataImpl();
    }

    @Bean
    MapUserDataToDomain mapUserDataToDomain() {
        return new MapUserDataToDomainImpl();
    }
}
