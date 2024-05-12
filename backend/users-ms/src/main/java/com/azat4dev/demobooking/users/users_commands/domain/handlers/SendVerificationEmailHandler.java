package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.Policy;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailBody;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailService;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendVerificationEmailHandler implements Policy<DomainEventNew<SendVerificationEmail>> {

    private final EmailAddress fromAddress;
    private final String fromName;
    private final EmailService emailService;

    @Override
    public void execute(DomainEventNew<SendVerificationEmail> command) {

        final var payload = command.payload();

        emailService.send(
            payload.email(),
            new EmailData(
                fromAddress,
                fromName,
                "Welcome to our platform",
                new EmailBody(
                    "Hello, " + payload.fullName().toString() + "!\n\n" +
                    "Please verify your email by clicking the link below:\n\n" +
                    "http://localhost:8080/users/verify?userId=" + command.payload().userId().toString()
                )
            )
        );
    }
}
