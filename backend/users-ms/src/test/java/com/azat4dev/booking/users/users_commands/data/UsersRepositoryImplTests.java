package com.azat4dev.booking.users.users_commands.data;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.users.users_commands.data.repositories.MapUserDataToDomain;
import com.azat4dev.booking.users.users_commands.data.repositories.MapUserToData;
import com.azat4dev.booking.users.users_commands.data.repositories.MapUserToDataImpl;
import com.azat4dev.booking.users.users_commands.data.repositories.UsersRepositoryImpl;
import com.azat4dev.booking.users.users_commands.data.repositories.dao.UsersDao;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.azat4dev.booking.users.users_commands.domain.UserHelpers.anyUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

public class UsersRepositoryImplTests {

    SUT createSUT() {

        final var mapUserToData = new MapUserToDataImpl();
        final var mapUserDataToDomain = mock(MapUserDataToDomain.class);
        final var usersDao = mock(UsersDao.class);

        return new SUT(
            new UsersRepositoryImpl(
                mapUserToData,
                mapUserDataToDomain,
                usersDao
            ),
            usersDao,
            mapUserToData,
            mapUserDataToDomain
        );
    }

    @Test
    public void test_addNew_givenNotExistingUser_thenThrowException() throws Exception {

        // Given
        final var sut = createSUT();
        final var newUser = anyUser();

        final var persistentUserData = sut.mapUserToData.map(newUser);

        willDoNothing().given(sut.usersDao).addNew(any());

        // When
        sut.repository.addNew(newUser);

        // Then
        then(sut.usersDao).should(times(1))
            .addNew(persistentUserData);
    }

    @Test
    public void test_addNew_givenExistingUserWithSameEmailAndDifferentId_thenThrowException() throws Exception {

        // Given
        final var sut = createSUT();
        final var newUser = anyUser();

        willThrow(new UsersDao.Exception.UserAlreadyExists()).given(sut.usersDao)
            .addNew(any());

        // When
        final var exception = assertThrows(UsersRepository.Exception.UserWithSameEmailAlreadyExists.class,
            () -> sut.repository.addNew(newUser));

        // Then
        assertThat(exception).isInstanceOf(UsersRepository.Exception.UserWithSameEmailAlreadyExists.class);
    }

    @Test
    public void test_update_givenNotExistingUser_thenThrowException() throws Exception {

        // Given
        final var sut = createSUT();
        final var newUser = anyUser();

        willThrow(new UsersDao.Exception.UserNotFound()).given(sut.usersDao)
            .update(any());

        // When
        final var exception = assertThrows(UsersRepository.Exception.UserNotFound.class,
            () -> sut.repository.update(newUser));

        // Then
        assertThat(exception).isInstanceOf(UsersRepository.Exception.UserNotFound.class);
    }

    @Test
    public void test_update_givenExistingUser_thenUpdateUser() throws Exception {

        // Given
        final var sut = createSUT();
        final var newUser = anyUser();

        final var persistentUserData = sut.mapUserToData.map(newUser);
        willDoNothing().given(sut.usersDao).update(any());

        // When
        sut.repository.update(newUser);

        // Then
        then(sut.usersDao).should(times(1))
            .update(persistentUserData);
    }

    @Test
    void test_findByEmail_givenEmptyDb_thenReturnEmptyOptional() {

        // Given
        final var sut = createSUT();
        final var email = UserHelpers.anyValidEmail();

        given(sut.usersDao.findByEmail(any()))
            .willReturn(Optional.empty());

        // When
        final var result = sut.repository.findByEmail(email);

        // Then
        then(sut.usersDao).should(times(1))
            .findByEmail(email.getValue());

        assertThat(result).isEmpty();
    }

    @Test
    void test_findByEmail_givenExistingUser_thenReturnEmptyOptional() throws DomainException {

        // Given
        final var sut = createSUT();
        final var expectedUser = anyUser();
        final var email = expectedUser.getEmail();

        final var persistentUserData = sut.mapUserToData.map(expectedUser);

        given(sut.usersDao.findByEmail(any()))
            .willReturn(Optional.of(persistentUserData));

        given(sut.mapUserDataToDomain.map(any()))
            .willReturn(expectedUser);

        // When
        final var result = sut.repository.findByEmail(email);

        // Then
        then(sut.usersDao).should(times(1))
            .findByEmail(email.getValue());

        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    @Test
    void test_findById_givenEmptyDb_thenReturnEmptyOptional() {

        final var sut = createSUT();
        final var id = UserHelpers.anyValidUserId();

        given(sut.usersDao.findById(any()))
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
        final var expectedUser = anyUser();
        final var persistentUserData = sut.mapUserToData.map(expectedUser);

        given(sut.usersDao.findById(any()))
            .willReturn(Optional.of(persistentUserData));

        given(sut.mapUserDataToDomain.map(any()))
            .willReturn(expectedUser);

        // When
        final var result = sut.repository.findById(expectedUser.getId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).isEqualTo(expectedUser);
    }

    record SUT(
        UsersRepository repository,
        UsersDao usersDao,
        MapUserToData mapUserToData,
        MapUserDataToDomain mapUserDataToDomain
    ) {
    }
}
