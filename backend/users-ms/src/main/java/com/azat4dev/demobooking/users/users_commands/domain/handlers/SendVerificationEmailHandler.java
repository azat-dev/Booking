package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.*;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationTokensService;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

@RequiredArgsConstructor
public class SendVerificationEmailHandler implements CommandHandler<DomainEventNew<SendVerificationEmail>> {

    private static final Logger logger = LoggerFactory.getLogger(SendVerificationEmailHandler.class);

    private final URL baseVerificationLinkUrl;
    private final EmailAddress fromAddress;
    private final String fromName;
    private final EmailService emailService;
    private final EmailVerificationTokensService emailVerificationTokensService;
    private final DomainEventsBus bus;
    private final DomainEventsFactory domainEventsFactory;

    @Override
    public void handle(DomainEventNew<SendVerificationEmail> command) {

        final var payload = command.payload();

        final var token = emailVerificationTokensService.generateFor(payload.userId(), payload.email());

        try {
            final var verificationLink = new URL(
                baseVerificationLinkUrl,
                "&token=" + token.toString()
            );

            emailService.send(
                payload.email(),
                new EmailData(
                    fromAddress,
                    fromName,
                    "Welcome to our platform",
                    new EmailBody(
                        "Hello, " + payload.fullName().toString() + "!\n\n" +
                        "Please verify your email by clicking the link below:\n\n" +
                        verificationLink.toString()
                    )
                )
            );

        } catch (Throwable e) {
            logger.error("Can' send verification email", e);

            bus.publish(
                domainEventsFactory.issue(
                    new FailedToSendVerificationEmail(
                        payload.userId(),
                        payload.email(),
                        payload.attempt() + 1
                    )
                )
            );
            return;
        }

        bus.publish(
            domainEventsFactory.issue(
                new VerificationEmailSent(
                    payload.userId(),
                    payload.email()
                )
            )
        );
    }
}
