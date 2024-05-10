package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.users.users_commands.application.config.DataConfig;
import com.azat4dev.demobooking.users.users_commands.data.repositories.UnitOfWorkImpl;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.OutboxEventsRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UnitOfWork;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import static com.azat4dev.demobooking.users.users_commands.data.DataHelpers.anyNewUserData;
import static org.assertj.core.api.Assertions.assertThat;

@Import({DataConfig.class, TestObjectMapperConfig.class})
@DataJpaTest
public class UnitOfWorkImplTests {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OutboxEventsRepository outboxEventsRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    SUT createSUT() {
        var jpaUnitOfWork = new UnitOfWorkImpl(
            platformTransactionManager,
            outboxEventsRepository,
            usersRepository
        );

        return new SUT(jpaUnitOfWork);
    }

    @Test
    void test_rollback_givenWriteOperation_thenRollbackAllWrites() {
        // Given
        var sut = createSUT();
        final var newUserData = anyNewUserData();

        final var usersRepository = sut.unitOfWork.getUsersRepository();
        usersRepository.createUser(newUserData);

        // When
        sut.unitOfWork.rollback();

        // Then
        assertThat(usersRepository.findById(newUserData.userId())).isEmpty();
    }

    @Test
    void test_save_givenWriteOperation_thenPerformWriteToDb() {
        // Given
        var sut = createSUT();
        final var newUserData = anyNewUserData();

        final var usersRepository = sut.unitOfWork.getUsersRepository();
        usersRepository.createUser(newUserData);

        // When
        sut.unitOfWork.save();

        // Then
        assertThat(usersRepository.findById(newUserData.userId())).isNotEmpty();
    }

    record SUT(
        UnitOfWork unitOfWork
    ) {
    }
}

class TestObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}