package com.azat4dev.booking.users.commands.infrastructure.persistence.repositories;

import com.azat4dev.booking.shared.data.repositories.outbox.OutboxEventsRepository;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.events.DomainEventsFactoryImpl;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.config.users_commands.infrastructure.CommonBeansConfig;
import com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.dao.OutboxEventsDaoConfig;
import com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.dao.UsersDaoConfig;
import com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.repositories.OutboxEventsRepositoryConfig;
import com.azat4dev.booking.users.config.users_commands.infrastructure.persistence.repositories.UsersRepositoryConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Import({CommonBeansConfig.class, UsersDaoConfig.class, UsersRepositoryConfig.class,
    OutboxEventsRepositoryConfig.class, OutboxEventsRepositoryConfig.class, OutboxEventsDaoConfig.class})
@JdbcTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UnitOfWorkImplTests {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OutboxEventsRepository outboxEventsRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @MockBean
    JwtDataEncoder jwtDataEncoder;

    UnitOfWork createSUT() {

        return new UnitOfWorkImpl(
            transactionTemplate,
            outboxEventsRepository,
            usersRepository
        );
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    void test_doOrFail_givenWriteOperation_thenRollbackAllWrites() {
        // Given
        var sut = createSUT();
        final var newUser = UserHelpers.anyUser();
        final var usersRepository = sut.getUsersRepository();

        // When
        assertThrows(RuntimeException.class, () -> {
            sut.doOrFail(() -> {
                usersRepository.addNew(UserHelpers.anyUser());
                throw new RuntimeException("Rollback");
            });
        });

        // Then
        final var sut2 = createSUT();
        final var foundUserData = sut2.getUsersRepository().findById(newUser.getId());
        assertThat(foundUserData).isEmpty();
    }

    @Test
    @Sql(scripts = {"/db/drop-schema.sql"})
    @Sql(scripts = {"/db/schema.sql"})
    void test_doOrFail_givenWriteOperation_thenPerformWriteToDb() throws Exception {
        // Given
        var sut = createSUT();
        final var newUser = UserHelpers.anyUser();

        final var usersRepository = sut.getUsersRepository();

        // When
        sut.doOrFail(() -> {
            usersRepository.addNew(newUser);
            return null;
        });

        // Then
        assertThat(usersRepository.findById(newUser.getId())).isNotEmpty();
    }

    record SUT(
        UnitOfWork unitOfWork
    ) {
    }

    @TestConfiguration
    public static class Config {

        @Bean
        DomainEventsFactory domainEventsFactory() {
            return new DomainEventsFactoryImpl(
                new RandomEventIdGenerator(),
                new SystemTimeProvider()
            );
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}