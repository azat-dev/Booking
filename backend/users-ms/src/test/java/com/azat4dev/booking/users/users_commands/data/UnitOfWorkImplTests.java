package com.azat4dev.booking.users.users_commands.data;

import com.azat4dev.booking.shared.domain.event.DomainEventsFactory;
import com.azat4dev.booking.shared.domain.event.DomainEventsFactoryImpl;
import com.azat4dev.booking.shared.domain.event.RandomEventIdGenerator;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtDataEncoder;
import com.azat4dev.booking.users.users_commands.application.config.data.DaoConfig;
import com.azat4dev.booking.users.users_commands.application.config.data.DataConfig;
import com.azat4dev.booking.users.users_commands.data.jpa.PostgresTest;
import com.azat4dev.booking.users.users_commands.data.repositories.UnitOfWorkImpl;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
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
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;


@Import({DataConfig.class, DaoConfig.class})
@JdbcTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:15-alpine:///"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UnitOfWorkImplTests {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OutboxEventsRepository outboxEventsRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @MockBean
    JwtDataEncoder jwtDataEncoder;

    UnitOfWork createSUT() {

        return new UnitOfWorkImpl(
            platformTransactionManager,
            outboxEventsRepository,
            usersRepository
        );
    }

    @Test
    @Sql("/db/drop-schema.sql")
    @Sql("/db/schema.sql")
    void test_rollback_givenWriteOperation_thenRollbackAllWrites() {
        // Given

        var sut = createSUT();
        final var newUser = UserHelpers.anyUser();

        final var usersRepository = sut.getUsersRepository();
        usersRepository.addNew(newUser);

        // When
        sut.rollback();

        // Then
        final var sut2 = createSUT();
        final var foundUserData = sut2.getUsersRepository().findById(newUser.getId());
        assertThat(foundUserData).isEmpty();
    }

    @Test
    @Sql(scripts = {"/db/drop-schema.sql"})
    @Sql(scripts = {"/db/schema.sql"})
    void test_save_givenWriteOperation_thenPerformWriteToDb() {
        // Given
        var sut = createSUT();
        final var newUser = UserHelpers.anyUser();

        final var usersRepository = sut.getUsersRepository();
        usersRepository.addNew(newUser);

        // When
        sut.save();

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