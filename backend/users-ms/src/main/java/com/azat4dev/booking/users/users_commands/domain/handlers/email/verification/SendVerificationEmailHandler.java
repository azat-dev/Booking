package com.azat4dev.booking.users.users_commands.domain.handlers.email.verification;

import com.azat4dev.booking.shared.domain.CommandHandler;
import com.azat4dev.booking.shared.domain.event.DomainEventsBus;
import com.azat4dev.booking.shared.domain.event.EventId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailBody;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils.BuildEmailVerificationLink;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils.ProvideEmailVerificationToken;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class SendVerificationEmailHandler implements CommandHandler<SendVerificationEmail> {

    private static final Logger logger = LoggerFactory.getLogger(SendVerificationEmailHandler.class);

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
                        verificationLink
                    )
                )
            );

        } catch (Throwable e) {
            logger.error("Can' send verification email", e);

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
}
