package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.users.users_commands.data.entities.UserData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.MapNewUserToData;
import com.azat4dev.demobooking.users.users_commands.data.repositories.MapUserDataToDomain;
import com.azat4dev.demobooking.users.users_commands.data.repositories.UsersRepositoryImpl;
import com.azat4dev.demobooking.users.users_commands.data.repositories.jpa.JpaUsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.azat4dev.demobooking.users.users_commands.data.DataHelpers.anyNewUserData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class UsersRepositoryImplTests {

    SUT createSUT() {

        final var mapNewUserData = mock(MapNewUserToData.class);
        final var mapUserDataToDomain = mock(MapUserDataToDomain.class);
        final var jpaUsersRepository = mock(JpaUsersRepository.class);

        return new SUT(
            new UsersRepositoryImpl(
                mapNewUserData,
                mapUserDataToDomain,
                jpaUsersRepository
            ),
            jpaUsersRepository,
            mapNewUserData,
            mapUserDataToDomain
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

    @Test
    public void test_createUser_givenExistingUserWithSameEmailAndDifferentId_thenThrowException() throws Exception {

        // Given
        final var sut = createSUT();
        final var newUserData = anyNewUserData();
        final var persistentUserData = new UserData();

        persistentUserData.setId(UserHelpers.anyValidUserId().value());
        persistentUserData.setEmail(newUserData.email().getValue());

        given(sut.mapNewUserToData.map(any()))
            .willReturn(persistentUserData);

        given(sut.jpaUsersRepository.saveAndFlush(persistentUserData))
            .willThrow(new DataIntegrityViolationException("User exists"));

        given(sut.jpaUsersRepository.findByEmail(any()))
            .willReturn(Optional.of(persistentUserData));

        // When
        assertThrows(UsersRepository.UserWithSameEmailAlreadyExistsException.class,
            () -> sut.repository.createUser(newUserData));

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByEmail(newUserData.email().getValue());
    }

    @Test
    void test_findByEmail_givenEmptyDb_thenReturnEmptyOptional() {

        // Given
        final var sut = createSUT();
        final var email = UserHelpers.anyValidEmail();

        given(sut.jpaUsersRepository.findByEmail(any()))
            .willReturn(Optional.empty());

        // When
        final var result = sut.repository.findByEmail(email);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByEmail(email.getValue());

        assertThat(result).isEmpty();
    }

    @Test
    void test_findByEmail_givenExistingUser_thenReturnEmptyOptional() throws DomainException {

        // Given
        final var sut = createSUT();
        final var expectedUser = UserHelpers.anyUser();
        final var email = expectedUser.email();

        final var persistentUserData = new UserData();

        given(sut.jpaUsersRepository.findByEmail(any()))
            .willReturn(Optional.of(persistentUserData));

        given(sut.mapUserDataToDomain.map(any()))
            .willReturn(expectedUser);

        // When
        final var result = sut.repository.findByEmail(email);

        // Then
        then(sut.jpaUsersRepository).should(times(1))
            .findByEmail(email.getValue());

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    @Test
    void test_findById_givenEmptyDb_thenReturnEmptyOptional() {

        final var sut = createSUT();
        final var id = UserHelpers.anyValidUserId();

        given(sut.jpaUsersRepository.findById(any()))
            .willReturn(Optional.empty());

        // When
        final var result = sut.repository.findById(id);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void test_findById_givenExistingUser_thenReturnUser() throws DomainException {

        // Given
        final var sut = createSUT();
        final var expectedUser = UserHelpers.anyUser();

        given(sut.jpaUsersRepository.getReferenceById(any()))
            .willReturn(new UserData());

        given(sut.mapUserDataToDomain.map(any()))
            .willReturn(expectedUser);

        // When
        final var result = sut.repository.findById(expectedUser.id());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    record SUT(
        UsersRepository repository,
        JpaUsersRepository jpaUsersRepository,
        MapNewUserToData mapNewUserToData,
        MapUserDataToDomain mapUserDataToDomain
    ) {
    }
}
