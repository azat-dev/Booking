package com.azat4dev.booking.users.queries.presentation.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.infrastructure.api.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.queries.domain.services.UsersQueryService;
import com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest.CurrentUserApi;
import com.azat4dev.booking.users.queries.infrastructure.presentation.api.rest.mappers.MapPersonalUserInfoToDTOImpl;
import com.azat4dev.booking.usersms.generated.server.api.QueriesCurrentUserApiDelegate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class UsersQueriesControllerTests {

    private SUT createSUT() {

        final var currentUserId = mock(CurrentAuthenticatedUserIdProvider.class);
        final var usersQueryService = mock(UsersQueryService.class);

        return new SUT(
            new CurrentUserApi(
                currentUserId,
                usersQueryService,
                new MapPersonalUserInfoToDTOImpl()
            ),
            currentUserId,
            usersQueryService
        );
    }

    @Test
    public void test_getCurrentUserInfo_givenUserDoesNotExist_thenReturn404() throws Exception {

        // Given
        final var sut = createSUT();
        final var userId = UserHelpers.anyValidUserId();

        given(sut.currentUserId.get())
            .willReturn(Optional.of(userId));

        given(sut.usersService.getPersonalInfoById(any()))
            .willReturn(Optional.empty());

        // When
        final var exception = assertThrows(
            ControllerException.class,
            () -> sut.api.getCurrentUser()
        );

        // Then
        assertThat(exception.status())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void test_getCurrentUserInfo_givenUserIsNotAuthenticated_thenReturn401() throws Exception {

        // Given
        final var sut = createSUT();

        given(sut.currentUserId.get())
            .willReturn(Optional.empty());

        // When
        final var exception = assertThrows(
            ControllerException.class,
            () -> sut.api.getCurrentUser()
        );

        // Then
        assertThat(exception.status())
            .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private record SUT(
        QueriesCurrentUserApiDelegate api,
        CurrentAuthenticatedUserIdProvider currentUserId,
        UsersQueryService usersService
    ) {
    }
}
