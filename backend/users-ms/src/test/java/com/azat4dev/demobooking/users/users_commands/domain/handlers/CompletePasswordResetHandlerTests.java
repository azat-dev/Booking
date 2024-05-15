package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.EventHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.Password;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.PasswordResetToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CompletePasswordResetHandlerTests {

    SUT createSUT() {

        final var usersRepository = mock(UsersRepository.class);
        final var validatePasswordResetTokenAnGetUserId = mock(ValidatePasswordResetTokenAnGetUserId.class);
        final var bus = mock(DomainEventsBus.class);
        final var passwordService = mock(PasswordService.class);

        return new SUT(
            new CompletePasswordResetHandler(
                validatePasswordResetTokenAnGetUserId,
                usersRepository,
                passwordService,
                bus
            ),
            usersRepository,
            validatePasswordResetTokenAnGetUserId,
            passwordService,
            bus
        );
    }

    CompletePasswordReset anyCommand() {
        return new CompletePasswordReset(
            "idempotentOperationToken",
            Password.makeFromString("newPassword"),
            PasswordResetToken.dangerouslyMakeFrom("invalidToken")
        );
    }

    @Test
    void test_handle_givenNotValidToken_thenPublishFailedPasswordResetEventAndThrowException() {

        // Given
        var sut = createSUT();

        final var command = anyCommand();

        given(sut.validatePasswordResetTokenAnGetUserId.execute(any()))
            .willThrow(new ValidatePasswordResetTokenAnGetUserId.InvalidTokenException());

        // When
        final var exception = assertThrows(CompletePasswordResetHandler.InvalidTokenException.class, () -> {
            sut.handler.handle(
                command,
                EventHelpers.anyEventId(),
                LocalDateTime.now()
            );
        });

        // Then

        assertThat(exception).isInstanceOf(CompletePasswordResetHandler.InvalidTokenException.class);
    }

    @Test
    void test_handle_givenValidToken_thenUpdateUserAndPublishPasswordDidResetEvent() {

        // Given
        final var sut = createSUT();
        final var command = anyCommand();
        final var user = UserHelpers.anyUser();
        final var userId = user.getId();
        final var encodedPassword = new EncodedPassword("encodedPassword");

        given(sut.validatePasswordResetTokenAnGetUserId.execute(any()))
            .willReturn(userId);

        // When
        sut.handler.handle(
            command,
            EventHelpers.anyEventId(),
            LocalDateTime.now()
        );

        // Then

        then(sut.usersRepository).should(times(1))
            .update(assertArg(u -> {
                assertThat(u.getId()).isEqualTo(userId);
                assertThat(u.getEncodedPassword()).isEqualTo(encodedPassword);
            }));

        then(sut.bus).should(times(1))
            .publish(new UserDidResetPassword(userId));
    }

    record SUT(
        CompletePasswordResetHandler handler,
        UsersRepository usersRepository,
        ValidatePasswordResetTokenAnGetUserId validatePasswordResetTokenAnGetUserId,
        PasswordService passwordService,
        DomainEventsBus bus
    ) {
    }
}
