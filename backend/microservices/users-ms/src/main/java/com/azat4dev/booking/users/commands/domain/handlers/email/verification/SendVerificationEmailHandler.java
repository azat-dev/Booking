package com.azat4dev.booking.users.commands.domain.handlers.email.verification;

import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailBody;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.BuildEmailVerificationLink;
import com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils.ProvideEmailVerificationToken;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Observed
@Slf4j
@RequiredArgsConstructor
public class SendVerificationEmailHandler implements CommandHandler<SendVerificationEmail> {

    private final BuildEmailVerificationLink buildVerificationLink;
    private final EmailAddress fromAddress;
    private final String fromName;
    private final EmailService emailService;
    private final ProvideEmailVerificationToken provideEmailVerificationToken;
    private final DomainEventsBus bus;

    @Override
    public void handle(SendVerificationEmail command, EventId eventId, LocalDateTime issuedAt) {

        final var token = provideEmailVerificationToken.execute(command.userId(), command.email());

        try {
            final var verificationLink = buildVerificationLink.execute(token);

            emailService.send(
                command.email(),
                new EmailService.EmailData(
                    fromAddress,
                    fromName,
                    "Welcome to our platform",
                    new EmailBody(
                        "Hello, " + command.fullName().toString() + "!\n\n" +
                            "Please verify your email by clicking the link below:\n\n" +
                            "<a href=\"" + verificationLink + "\">Verify email</a>\n\n"
                    )
                )
            );

            log.atInfo()
                .addArgument(command::userId)
                .log("Verification email sent: userId={}");

        } catch (Exception e) {
            log.atError()
                .setCause(e)
                .addArgument(command::userId)
                .log("Can' send verification email: userId={}");

            bus.publish(
                new FailedToSendVerificationEmail(
                    command.userId(),
                    command.email(),
                    command.attempt() + 1
                )
            );
            return;
        }

        bus.publish(
            new VerificationEmailSent(
                command.userId(),
                command.email()
            )
        );
    }

    @Override
    public Class<SendVerificationEmail> getCommandClass() {
        return SendVerificationEmail.class;
    }
}
