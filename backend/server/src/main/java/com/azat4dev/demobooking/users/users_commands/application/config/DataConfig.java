package com.azat4dev.demobooking.users.users_commands.application.config;

import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.users_commands.data.repositories.*;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaOutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaUsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailData;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

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

    @Bean
    DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new DomainEventSerializerImpl(objectMapper);
    }

    @Bean
    OutboxEventsRepository outboxEventsRepository(
        TimeProvider timeProvider,
        DomainEventSerializer domainEventSerializer,
        JpaOutboxEventsRepository jpaOutboxEventsRepository
    ) {


        return new OutboxEventsRepositoryImpl(
            timeProvider,
            domainEventSerializer,
            jpaOutboxEventsRepository
        );
    }

    @Bean
    UnitOfWork unitOfWork(
        PlatformTransactionManager transactionManager,
        OutboxEventsRepository outboxEventsRepository,
        UsersRepository usersRepository
    ) {
        return new UnitOfWorkImpl(
            transactionManager,
            outboxEventsRepository,
            usersRepository
        );
    }
}
