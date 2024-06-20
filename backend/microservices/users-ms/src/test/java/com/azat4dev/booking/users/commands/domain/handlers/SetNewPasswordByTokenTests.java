package com.azat4dev.booking.users.commands.domain.handlers;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.commands.domain.core.events.FailedToCompleteResetPassword;
import com.azat4dev.booking.users.commands.domain.core.events.UserDidResetPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SetNewPasswordByToken;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SetNewPasswordByTokenImpl;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils.ValidateTokenForPasswordResetAndGetUserId;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.commands.domain.interfaces.services.PasswordService;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class SetNewPasswordByTokenTests {

    SUT createSUT() {

        final var usersRepository = mock(UsersRepository.class);
        final var validatePasswordResetTokenAnGetUserId = mock(ValidateTokenForPasswordResetAndGetUserId.class);
        final var bus = mock(DomainEventsBus.class);
        final var passwordService = mock(PasswordService.class);

        return new SUT(
            new SetNewPasswordByTokenImpl(
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

    IdempotentOperationId anyIdempotentOperationId() throws IdempotentOperationId.Exception {
        return IdempotentOperationId.checkAndMakeFrom(UUID.randomUUID().toString());
    }

    EncodedPassword anyPassword() {
        return new EncodedPassword("encodedPassword");
    }

    TokenForPasswordReset anyToken() {
        return TokenForPasswordReset.dangerouslyMakeFrom("token");
    }

    @Test
    void test_execute_givenNotValidToken_thenPublishFailedPasswordResetEventAndThrowException() throws ValidateTokenForPasswordResetAndGetUserId.Exception, IdempotentOperationId.Exception {

        // Given
        var sut = createSUT();


        final var operationId = anyIdempotentOperationId();
        final var token = anyToken();
        final var password = anyPassword();

        given(sut.validateTokenForPasswordResetAndGetUserId.execute(any()))
            .willThrow(new ValidateTokenForPasswordResetAndGetUserId.Exception.InvalidToken());

        // When
        final var exception = assertThrows(SetNewPasswordByToken.Exception.InvalidToken.class, () -> {
            sut.resetPassword.execute(
                operationId,
                token,
                password
            );
        });

        // Then
        assertThat(exception).isInstanceOf(SetNewPasswordByToken.Exception.InvalidToken.class);

        then(sut.bus).should(times(1))
            .publish(new FailedToCompleteResetPassword(operationId));
    }

    @Test
    void test_handle_givenValidToken_thenUpdateUserAndPublishPasswordDidResetEvent() throws ValidateTokenForPasswordResetAndGetUserId.Exception, SetNewPasswordByToken.Exception, IdempotentOperationId.Exception {

        // Given
        final var sut = createSUT();

        final var operationId = anyIdempotentOperationId();
        final var token = anyToken();
        final var password = anyPassword();
        final var user = UserHelpers.anyUser();
        final var userId = user.getId();
        final var encodedPassword = new EncodedPassword("encodedPassword");

        given(sut.validateTokenForPasswordResetAndGetUserId.execute(any()))
            .willReturn(userId);

        given(sut.usersRepository.findById(any()))
            .willReturn(Optional.of(user));

        given(sut.passwordService.encodePassword(any()))
            .willReturn(encodedPassword);

        // When
        sut.resetPassword.execute(
            operationId,
            token,
            password
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
        SetNewPasswordByToken resetPassword,
        UsersRepository usersRepository,
        ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId,
        PasswordService passwordService,
        DomainEventsBus bus
    ) {
    }
}
