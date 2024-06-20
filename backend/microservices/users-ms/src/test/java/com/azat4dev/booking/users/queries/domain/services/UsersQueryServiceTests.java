package com.azat4dev.booking.users.queries.domain.services;

import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.queries.domain.entities.PersonalUserInfo;
import com.azat4dev.booking.users.queries.domain.entities.UserPhoto;
import com.azat4dev.booking.users.queries.domain.interfaces.repositories.UsersReadRepository;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class UsersQueryServiceTests {

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

    UserPhoto anyUserPhoto() {
        try {
            return new UserPhoto(
                URI.create("https://example.com/photo.jpg").toURL()
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    PersonalUserInfo anyPersonalUserInfo() {
        return new PersonalUserInfo(
            UserHelpers.anyValidUserId(),
            anyEmail(),
            PersonalUserInfo.EmailVerificationStatus.VERIFIED,
            UserHelpers.anyFullName(),
            Optional.of(anyUserPhoto())
        );
    }

    @Test
    void test_getPersonalInfoById_givenNotExistingUser_thenReturnEmpty() {
        // Given
        final var sut = makeSUT();
        final var userId = UserHelpers.anyValidUserId();

        given(sut.usersReadRepository.getPersonalUserInfoById(any()))
            .willReturn(Optional.empty());

        // When
        final var result = sut.service.getPersonalInfoById(userId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void test_getPersonalInfoById_givenExistingUser_thenReturnUser() {
        // Given
        final var sut = makeSUT();
        final var existingUser = anyPersonalUserInfo();

        given(sut.usersReadRepository.getPersonalUserInfoById(any()))
            .willReturn(Optional.of(existingUser));

        // When
        final var result = sut.service.getPersonalInfoById(existingUser.id());

        // Then
        assertThat(result.orElseThrow()).isEqualTo(existingUser);
    }

    record SUT(
        UsersQueryService service,
        UsersReadRepository usersReadRepository
    ) {
    }
}
