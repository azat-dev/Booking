package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.EventHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.ResetPasswordByEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.SentEmailForPasswordReset;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailData;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.azat4dev.demobooking.users.users_commands.domain.UserHelpers.anyUser;
import static com.azat4dev.demobooking.users.users_commands.domain.UserHelpers.anyValidEmail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ResetPasswordByEmailHandlerTests {

    record SUT(
        ResetPasswordByEmailHandler handler,
        UsersRepository usersRepository,
        BuildResetPasswordEmail buildResetPasswordEmail,
        EmailService emailService,
        DomainEventsBus bus
    ) {
    }

    SUT createSUT() {

        final var usersRepository = mock(UsersRepository.class);
        final var buildResetPasswordEmail = mock(BuildResetPasswordEmail.class);
        final var emailService = mock(EmailService.class);
        final var bus = mock(DomainEventsBus.class);

        return new SUT(
            new ResetPasswordByEmailHandler(
                usersRepository,
                buildResetPasswordEmail,
                emailService,
                bus
            ),
            usersRepository,
            buildResetPasswordEmail,
            emailService,
            bus
        );
    }

    @Test
    void test_handle_givenNotExistingEmail_thenThrowException() {

        // Given
        final var sut = createSUT();

        final var command = new ResetPasswordByEmail(
            "idempotentOperationToken",
            anyValidEmail()
        );

        given(sut.usersRepository.findByEmail(any()))
            .willThrow(new ResetPasswordByEmailHandler.UserNotFoundException());

        // When
        final var exception = assertThrows(ResetPasswordByEmailHandler.UserNotFoundException.class, () -> {
            sut.handler.handle(
                command,
                EventHelpers.anyEventId(),
                LocalDateTime.now()
            );
        });

        // Then
        assertThat(exception).isInstanceOf(ResetPasswordByEmailHandler.UserNotFoundException.class);
    }

    @Test
    void test_handle_givenExistingEmail_thenSendEmail() {

        // Given
        final var sut = createSUT();
        final var user = anyUser();

        final var command = new ResetPasswordByEmail(
            "idempotentOperationToken",
            user.getEmail()
        );

        given(sut.usersRepository.findByEmail(any()))
            .willReturn(Optional.of(user));

        final var expectedEmailData = new BuildResetPasswordEmail.EmailData(
            "fromName",
            user.getEmail(),
            "subject",
            new EmailBody("body")
        );

        given(sut.buildResetPasswordEmail.execute(any(), any()))
            .willReturn(expectedEmailData);

        // When
        sut.handler.handle(
            command,
            EventHelpers.anyEventId(),
            LocalDateTime.now()
        );

        // Then
        then(sut.buildResetPasswordEmail).should(times(1))
            .execute(user.getId(), user.getEmail());

        then(sut.emailService).should().send(
            user.getEmail(),
            new EmailData(
                expectedEmailData.fromAddress(),
                expectedEmailData.fromName(),
                expectedEmailData.subject(),
                expectedEmailData.body()
            )
        );

        then(sut.bus).should().publish(
            new SentEmailForPasswordReset(
                user.getId(),
                user.getEmail()
            )
        );
    }
}
