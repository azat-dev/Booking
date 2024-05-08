package com.azat4dev.demobooking.users.users_queries.domain.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_queries.domain.entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;

interface UsersReadRepository {
    Optional<User> getById(UserId id);

}

class UsersQueryServiceImpl implements UsersQueryService {

    private final UsersReadRepository usersReadRepository;

    UsersQueryServiceImpl(
        UsersReadRepository usersReadRepository
    ) {
        this.usersReadRepository = usersReadRepository;
    }

    @Override
    public Optional<User> getById(UserId id) {
        return usersReadRepository.getById(id);
    }
}

public class UsersQueryServiceTests {

    record SUT(
        UsersQueryService service,
        UsersReadRepository usersReadRepository
    ) {}

    SUT makeSUT() {
        final var usersRepository = mock(UsersReadRepository.class);
        return new SUT(
            new UsersQueryServiceImpl(usersRepository),
            usersRepository
        );
    }

    String anyEmail() {
        return "anyemail@gmail.com";
    }

    User anyUser() {
        return new User(
            UserHelpers.anyValidUserId(),
            UserHelpers.anyValidEmail().getValue()
        );
    }

    @Test
    void test_getById_givenNotExistingUser_thenReturnEmpty() {
        // Given
        final var sut = makeSUT();
        final var userId = UserHelpers.anyValidUserId();

        given(sut.usersReadRepository.getById(any()))
            .willReturn(Optional.empty());

        // When
        final var result = sut.service.getById(userId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void test_getById_givenExistingUser_thenReturnUser() {
        // Given
        final var sut = makeSUT();
        final var existingUser = anyUser();

        given(sut.usersReadRepository.getById(any()))
            .willReturn(Optional.of(existingUser));

        // When
        final var result = sut.service.getById(existingUser.id());

        // Then
        assertThat(result.orElseThrow()).isEqualTo(existingUser);
    }
}
