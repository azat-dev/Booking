package com.azat4dev.booking.users.commands.domain.policies;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.commands.domain.EventHelpers;
import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.SendVerificationEmailHandler;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.BuildEmailVerificationLink;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.ProvideEmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class SendVerificationEmailHandlerTests {

    SUT createSUT() {

        final var buildVerificationLink = mock(BuildEmailVerificationLink.class);
        final var verificationLink = "http://verificationLink";
        given(buildVerificationLink.execute(any()))
            .willReturn(verificationLink);

        final var fromAddress = UserHelpers.anyValidEmail();
        final var fromName = "verification";

        final var emailService = mock(EmailService.class);
        final var tokenService = mock(ProvideEmailVerificationToken.class);

        final var tokenValue = "emailVerificationToken";
        given(tokenService.execute(any(), any()))
            .willReturn(new EmailVerificationToken(tokenValue));

        final var domainEventsBus = mock(DomainEventsBus.class);

        final var handler = new SendVerificationEmailHandler(
            buildVerificationLink,
            fromAddress,
            fromName,
            emailService,
            tokenService,
            domainEventsBus
        );

        return new SUT(
            handler,
            buildVerificationLink,
            fromAddress,
            fromName,
            emailService,
            tokenService,
            domainEventsBus,
            verificationLink
        );
    }

    SendVerificationEmail anySendCommand() {
        return new SendVerificationEmail(
            UserHelpers.anyValidUserId(),
            UserHelpers.anyValidEmail(),
            UserHelpers.anyFullName(),
            0
        );
    }

    @Test
    void test_handle_givenSendSuccess_thenPublishSuccessEvent() throws MalformedURLException {
        // Given
        final var sut = createSUT();
        final var command = anySendCommand();

        // When
        sut.handler.handle(command, EventHelpers.anyEventId(), LocalDateTime.now());

        // Then
        then(sut.emailService).should(times(1))
            .send(
                eq(command.email()),
                assertArg(m -> {
                    assertThat(m.fromName()).contains(sut.fromName);
                    assertThat(m.from()).isEqualTo(sut.fromAddress);
                    assertThat(m.body().value()).contains(sut.verificationLink);
                })
            );

        final var expectedEvent = new VerificationEmailSent(
            command.userId(),
            command.email()
        );

        then(sut.bus).should(times(1))
            .publish(expectedEvent);
    }

    @Test
    void test_handle_givenSendFailed_thenPublishSuccessEvent() throws MalformedURLException {
        // Given
        final var sut = createSUT();
        final var command = anySendCommand();

        willThrow(new RuntimeException()).
            given(sut.emailService)
            .send(any(), any());

        // When
        sut.handler.handle(command, EventHelpers.anyEventId(), LocalDateTime.now());

        // Then
        final var expectedEvent = new FailedToSendVerificationEmail(
            command.userId(),
            command.email(),
            command.attempt() + 1
        );

        then(sut.bus).should(times(1))
            .publish(expectedEvent);
    }

    record SUT(
        SendVerificationEmailHandler handler,
        BuildEmailVerificationLink buildVerificationLink,
        EmailAddress fromAddress,
        String fromName,
        EmailService emailService,
        ProvideEmailVerificationToken tokenService,
        DomainEventsBus bus,
        String verificationLink
    ) {
    }
}
