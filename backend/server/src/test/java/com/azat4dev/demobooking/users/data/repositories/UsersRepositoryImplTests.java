package com.azat4dev.demobooking.users.data.repositories;

import com.azat4dev.demobooking.users.domain.UserHelpers;
import com.azat4dev.demobooking.users.domain.entities.User;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.NewUserData;
import com.azat4dev.demobooking.users.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.UserId;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;


@FunctionalInterface
interface MapNewUserToData {
    UserData map(NewUserData newUserData);
}

interface JpaUsersRepository extends JpaRepository<UserData, UUID> {

}

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
class UserData {

    private @Id UUID id;
    private @Nonnull String email;
    private @Nonnull String encodedPassword;

    public UserData() {
    }

    public UserData(
        UUID id,
        @Nonnull String email,
        @Nonnull String encodedPassword
    ) {
        this.id = id;
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nonnull String email) {
        this.email = email;
    }

    @Nonnull
    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(@Nonnull String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData userData)) return false;
        return Objects.equals(getId(), userData.getId()) && Objects.equals(getEmail(), userData.getEmail()) && Objects.equals(getEncodedPassword(), userData.getEncodedPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getEncodedPassword());
    }
}

class UsersRepositoryImpl implements UsersRepository {

    private final MapNewUserToData mapNewUserToData;
    private final JpaUsersRepository jpaUsersRepository;

    public UsersRepositoryImpl(
        MapNewUserToData mapNewUserToData,
        JpaUsersRepository jpaUsersRepository
    ) {
        this.mapNewUserToData = mapNewUserToData;
        this.jpaUsersRepository = jpaUsersRepository;
    }

    @Override
    public void createUser(NewUserData newUserData) throws UserAlreadyExistsException {

        final var userData = mapNewUserToData.map(newUserData);
        jpaUsersRepository.saveAndFlush(userData);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        return Optional.empty();
    }
}

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
