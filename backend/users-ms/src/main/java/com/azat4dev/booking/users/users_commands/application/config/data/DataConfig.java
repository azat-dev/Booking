package com.azat4dev.booking.users.users_commands.application.config.data;

import com.azat4dev.booking.shared.data.DomainEventSerializer;
import com.azat4dev.booking.shared.data.DomainEventSerializerImpl;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDao;
import com.azat4dev.booking.shared.data.dao.outbox.OutboxEventsDaoJdbc;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepositoryImpl;
import com.azat4dev.booking.shared.domain.event.DomainEventsFactory;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.users_commands.data.repositories.*;
import com.azat4dev.booking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.booking.users.users_commands.data.repositories.dto.DomainEventDTO;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils.ProvideResetPasswordToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils.ProvideResetPasswordTokenImpl;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWorkFactory;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class DataConfig {

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

    @Bean
    TimeProvider timeProvider() {
        return new SystemTimeProvider();
    }

    @Bean
    MapUserToData mapNewUserToData() {
        return new MapUserToDataImpl();
    }

    @Bean
    MapUserDataToDomain mapUserDataToDomain() {
        return new MapUserDataToDomainImpl();
    }

    @Bean
    DomainEventSerializer domainEventSerializer() {
        return new DomainEventSerializerImpl(
            DomainEventDTO::makeFrom,
            DomainEventDTO.class
        );
    }

    @Bean
    OutboxEventsDao outboxEventsDao(NamedParameterJdbcTemplate jdbcTemplate) {
        return new OutboxEventsDaoJdbc(jdbcTemplate);
    }

    @Bean
    OutboxEventsRepository outboxEventsRepository(
        DomainEventSerializer domainEventSerializer,
        OutboxEventsDao outboxEventsDao,
        DomainEventsFactory domainEventsFactory
    ) {
        return new OutboxEventsRepositoryImpl(
            domainEventSerializer,
            outboxEventsDao,
            domainEventsFactory
        );
    }

    @Bean
    UnitOfWorkFactory unitOfWorkFactory(
        TransactionTemplate transactionTemplate,
        OutboxEventsRepository outboxEventsRepository,
        UsersRepository usersRepository
    ) {

        return new UnitOfWorkFactory() {
            @Override
            public UnitOfWork make() {
                return new UnitOfWorkImpl(
                    transactionTemplate,
                    outboxEventsRepository,
                    usersRepository
                );
            }
        };
    }

    @Bean
    ProvideResetPasswordToken generateResetPasswordToken(
        @Value("${app.reset_password.token_expiration_in_ms}")
        long expirationInMs,
        JwtDataEncoder jwtDataEncoder,
        TimeProvider timeProvider
    ) {
        return new ProvideResetPasswordTokenImpl(
            expirationInMs,
            jwtDataEncoder,
            timeProvider
        );
    }
}
