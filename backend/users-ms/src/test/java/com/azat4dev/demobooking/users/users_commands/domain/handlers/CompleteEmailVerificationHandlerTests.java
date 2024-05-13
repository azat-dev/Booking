package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokensService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.azat4dev.demobooking.users.users_commands.domain.EventHelpers.eventsFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CompleteEmailVerificationHandlerTests {

    SUT createSUT() {

        final var tokensService = mock(EmailVerificationTokensService.class);
        final var usersRepository = mock(UsersRepository.class);

        final var bus = mock(DomainEventsBus.class);

        return new SUT(
            new CompleteEmailVerificationHandler(
                tokensService,
                usersRepository,
                bus
            ),
            tokensService,
            usersRepository,
            bus
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
        final var event = (DomainEventNew<CompleteEmailVerification>) eventsFactory.issue(command);

        willThrow(new EmailVerificationTokensService.TokenIsNotValidException())
            .given(sut.tokensService)
            .verify(any(), any(), any());

        // When
        final var exception = assertThrows(CompleteEmailVerificationHandler.TokenIsNotValidException.class, () -> {
            sut.handler.handle(event);
        });

        // Then
        assertThat(exception).isNotNull();
    }

    @Test
    void test_handle_givenValidToken_thenSetUserVerificationStatusAndPublishEvent() {

        // Given
        final var sut = createSUT();
        final var existingUser = UserHelpers.anyUser();
        final var command = anyCompleteEmailVerification();
        final var event = (DomainEventNew<CompleteEmailVerification>) eventsFactory.issue(command);

        given(sut.tokensService.verify(any(), any(), any()))
            .willReturn(true);

        given(sut.usersRepository.findById(any()))
            .willReturn(Optional.of(existingUser));

        // When
        sut.handler.handle(event);

        // Then
        then(sut.tokensService).should(times(1))
            .verify(command.getToken(), existingUser.getId(), existingUser.getEmail());

        then(sut.usersRepository)
            .should(times(1))
            .save(assertArg(u -> assertThat(u.emailVerificationStatus()).isEqualTo(EmailVerificationStatus.VERIFIED)));

        final var expectedEvent = new UserVerifiedEmail(existingUser.getId(), existingUser.getEmail());

        then(sut.bus)
            .should(times(1))
            .publish(assertArg(e -> assertThat(e.payload()).isEqualTo(expectedEvent)));
    }

    record SUT(
        CompleteEmailVerificationHandler handler,
        EmailVerificationTokensService tokensService,
        UsersRepository usersRepository,
        DomainEventsBus bus
    ) {
    }
}
