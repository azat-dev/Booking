package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.EventHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompletePasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.CompletePasswordResetHandler;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.PasswordService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

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
        final var validatePasswordResetTokenAnGetUserId = mock(ValidateTokenForPasswordResetAndGetUserId.class);
        final var bus = mock(DomainEventsBus.class);
        final var passwordService = mock(PasswordService.class);

        return new SUT(
            new CompletePasswordResetHandler(
                validatePasswordResetTokenAnGetUserId,
                usersRepository,
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
            new EncodedPassword("encodedPassword"),
            TokenForPasswordReset.dangerouslyMakeFrom("invalidToken")
        );
    }

    @Test
    void test_handle_givenNotValidToken_thenPublishFailedPasswordResetEventAndThrowException() throws ValidateTokenForPasswordResetAndGetUserId.Exception {

        // Given
        var sut = createSUT();

        final var command = anyCommand();

        given(sut.validateTokenForPasswordResetAndGetUserId.execute(any()))
            .willThrow(new ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken());

        // When
        final var exception = assertThrows(CompletePasswordResetHandler.Exception.InvalidToken.class, () -> {
            sut.handler.handle(
                command,
                EventHelpers.anyEventId(),
                LocalDateTime.now()
            );
        });

        // Then
        assertThat(exception).isInstanceOf(CompletePasswordResetHandler.Exception.InvalidToken.class);

        then(sut.bus).should(times(1))
            .publish(new FailedToCompleteResetPassword(command.idempotentOperationToken()));
    }

    @Test
    void test_handle_givenValidToken_thenUpdateUserAndPublishPasswordDidResetEvent() throws ValidateTokenForPasswordResetAndGetUserId.Exception, CompletePasswordResetHandler.Exception {

        // Given
        final var sut = createSUT();
        final var command = anyCommand();
        final var user = UserHelpers.anyUser();
        final var userId = user.getId();

        given(sut.validateTokenForPasswordResetAndGetUserId.execute(any()))
            .willReturn(userId);

        given(sut.usersRepository.findById(any()))
            .willReturn(Optional.of(user));

        given(sut.passwordService.encodePassword(any()))
            .willReturn(command.newPassword());

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
                assertThat(u.getEncodedPassword()).isEqualTo(command.newPassword());
            }));

        then(sut.bus).should(times(1))
            .publish(new UserDidResetPassword(userId));
    }

    record SUT(
        CompletePasswordResetHandler handler,
        UsersRepository usersRepository,
        ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId,
        PasswordService passwordService,
        DomainEventsBus bus
    ) {
    }
}
