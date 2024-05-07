package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.users.data.entities.UserData;
import com.azat4dev.demobooking.users.data.repositories.jpa.JpaUsersRepository;
import com.azat4dev.demobooking.users.domain.UserHelpers;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;


public class UsersRepositoryImplTests {

    SUT createSUT() {

        final var mapNewUserData = mock(MapNewUserToData.class);
        final var jpaUsersRepository = mock(JpaUsersRepository.class);

        return new SUT(
            new UsersRepositoryImpl(
                mapNewUserData,
                jpaUsersRepository
            ),
            jpaUsersRepository,
            mapNewUserData
        );
    }

    NewUserData anyNewUserData() {
        return new NewUserData(
            UserHelpers.anyValidUserId(),
            new Date(),
            UserHelpers.anyValidEmail(),
            UserHelpers.anyEncodedPassword(),
            EmailVerificationStatus.NOT_VERIFIED
        );
    }

    @Test
    public void test_createUser_givenNotExistingUser_thenThrowException() throws Exception {

        // Given
        final var sut = createSUT();
        final var newUserData = anyNewUserData();

        final var persistentUserData = new UserData();

        given(sut.mapNewUserToData.map(any()))
            .willReturn(persistentUserData);

        given(sut.jpaUsersRepository.saveAndFlush(persistentUserData))
            .willReturn(persistentUserData);

        // When
        sut.repository.createUser(newUserData);

        // Then
        then(sut.mapNewUserToData()).should(times(1))
            .map(newUserData);

        then(sut.jpaUsersRepository).should(times(1))
            .saveAndFlush(persistentUserData);
    }

    record SUT(
        UsersRepository repository,
        JpaUsersRepository jpaUsersRepository,
        MapNewUserToData mapNewUserToData
    ) {
    }
}
