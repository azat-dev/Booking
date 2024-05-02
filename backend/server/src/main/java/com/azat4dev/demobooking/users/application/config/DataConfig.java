package com.azat4dev.demobooking.users.application.config;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventsStore;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.domain.services.EmailData;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
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

            @Override
            public Optional<User> findByEmail(EmailAddress email) {
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
}
