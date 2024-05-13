package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.EventHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.SendVerificationEmailHandler;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokensService;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class SendVerificationEmailHandlerTests {

    SUT createSUT() throws MalformedURLException {

        final var baseUrl = new URL("http://localhost:8080");
        final var fromAddress = UserHelpers.anyValidEmail();
        final var fromName = "verification";

        final var emailService = mock(EmailService.class);
        final var tokenService = mock(EmailVerificationTokensService.class);

        final var tokenValue = "emailVerificationToken";
        given(tokenService.generateFor(any(), any()))
            .willReturn(new EmailVerificationToken(tokenValue));
        final var domainEventsBus = mock(DomainEventsBus.class);


        final var handler = new SendVerificationEmailHandler(
            baseUrl,
            fromAddress,
            fromName,
            emailService,
            tokenService,
            domainEventsBus,
            EventHelpers.eventsFactory
        );

        return new SUT(
            handler,
            baseUrl,
            fromAddress,
            fromName,
            emailService,
            tokenService,
            domainEventsBus,
            tokenValue
        );
    }

    DomainEventNew<SendVerificationEmail> anySendCommand() {
        return (DomainEventNew<SendVerificationEmail>) EventHelpers.eventsFactory.issue(
            new SendVerificationEmail(
                UserHelpers.anyValidUserId(),
                UserHelpers.anyValidEmail(),
                UserHelpers.anyFullName(),
                0
            )
        );
    }

    @Test
    void test_handle_givenSendSuccess_thenPublishSuccessEvent() throws MalformedURLException {
        // Given
        final var sut = createSUT();
        final var command = anySendCommand();

        given(sut.tokenService.generateFor(any(), any()))
            .willReturn(new EmailVerificationToken("emailVerificationToken"));

        // When
        sut.handler.handle(command);

        // Then
        then(sut.emailService).should(times(1))
            .send(
                eq(command.payload().email()),
                assertArg(m -> {
                    assertThat(m.fromName()).contains(sut.fromName);
                    assertThat(m.from()).isEqualTo(sut.fromAddress);
                    assertThat(m.body().value()).contains(sut.emailVerificationToken);
                })
            );

        final var expectedEvent = new VerificationEmailSent(
            command.payload().userId(),
            command.payload().email()
        );

        then(sut.bus).should(times(1))
            .publish(assertArg(e -> assertThat(e.payload()).isEqualTo(expectedEvent)));
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
        sut.handler.handle(command);

        // Then
        final var expectedEvent = new FailedToSendVerificationEmail(
            command.payload().userId(),
            command.payload().email(),
            command.payload().attempt() + 1
        );

        then(sut.bus).should(times(1))
            .publish(assertArg(e -> assertThat(e.payload()).isEqualTo(expectedEvent)));
    }

    record SUT(
        SendVerificationEmailHandler handler,
        URL baseUrl,
        EmailAddress fromAddress,
        String fromName,
        EmailService emailService,
        EmailVerificationTokensService tokenService,
        DomainEventsBus bus,
        String emailVerificationToken
    ) {

    }
}
