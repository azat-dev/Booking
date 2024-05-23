package com.azat4dev.booking.users.users_commands.domain.handlers;

import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.utils.SystemTimeProvider;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.domain.EventHelpers;
import com.azat4dev.booking.users.users_commands.domain.UserHelpers;
import com.azat4dev.booking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.CompleteEmailVerificationHandler;
import com.azat4dev.booking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationTokenInfo;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils.GetInfoForEmailVerificationToken;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CompleteEmailVerificationHandlerTests {

    SUT createSUT() {

        final var getTokenInfo = mock(GetInfoForEmailVerificationToken.class);
        final var usersRepository = mock(UsersRepository.class);
        final var bus = mock(DomainEventsBus.class);

        final var timeProvider = new SystemTimeProvider();

        return new SUT(
            new CompleteEmailVerificationHandler(
                getTokenInfo,
                usersRepository,
                bus,
                timeProvider
            ),
            getTokenInfo,
            usersRepository,
            bus,
            timeProvider
        );
    }

    CompleteEmailVerification anyCompleteEmailVerification() {
        return new CompleteEmailVerification(
            new EmailVerificationToken("any")
        );
    }

    @Test
    void test_handle_givenTokenIsNotValid_thenThrowException() {

        // Given
        final var sut = createSUT();
        final var command = anyCompleteEmailVerification();

        willThrow(new GetInfoForEmailVerificationToken.TokenIsNotValidException())
            .given(sut.getTokenInfo)
            .execute(any());

        // When
        final var exception = assertThrows(CompleteEmailVerificationHandler.Exception.TokenIsNotValid.class, () -> {
            sut.handler.handle(command, EventHelpers.anyEventId(), LocalDateTime.now());
        });

        // Then
        assertThat(exception).isNotNull();
    }

    @Test
    void test_handle_givenValidToken_thenSetUserVerificationStatusAndPublishEvent() throws CompleteEmailVerificationHandler.Exception {

        // Given
        final var sut = createSUT();
        final var existingUser = UserHelpers.anyUser();
        final var command = anyCompleteEmailVerification();
        final var currentTime = LocalDateTime.now();

        final var tokenInfo = new EmailVerificationTokenInfo(
            existingUser.getId(),
            existingUser.getEmail(),
            currentTime.plusDays(100)
        );

        given(sut.getTokenInfo.execute(any()))
            .willReturn(tokenInfo);

        given(sut.usersRepository.findById(any()))
            .willReturn(Optional.of(existingUser));

        // When
        sut.handler.handle(command, EventHelpers.anyEventId(), currentTime);

        // Then
        then(sut.getTokenInfo).should(times(1))
            .execute(command.token());

        then(sut.usersRepository)
            .should(times(1))
            .update(assertArg(u -> assertThat(u.emailVerificationStatus()).isEqualTo(EmailVerificationStatus.VERIFIED)));

        final var expectedEvent = new UserVerifiedEmail(existingUser.getId(), existingUser.getEmail());

        then(sut.bus)
            .should(times(1))
            .publish(expectedEvent);
    }

    record SUT(
        CompleteEmailVerificationHandler handler,
        GetInfoForEmailVerificationToken getTokenInfo,
        UsersRepository usersRepository,
        DomainEventsBus bus,
        TimeProvider timeProvider
    ) {
    }
}
