package com.azat4dev.demobooking.users.domain.policies;

import com.azat4dev.demobooking.users.domain.events.UserCreated;
import com.azat4dev.demobooking.users.domain.services.EmailData;
import com.azat4dev.demobooking.users.domain.services.EmailService;
import com.azat4dev.demobooking.users.domain.services.VerificationEmailBuilder;

public class SendVerificationEmailPolicyImpl implements SendVerificationEmailPolicy {

    private final VerificationEmailBuilder emailBuilder;
    private final EmailService emailService;

    public SendVerificationEmailPolicyImpl(
        EmailService emailService,
        VerificationEmailBuilder emailBuilder
    ) {
        this.emailService = emailService;
        this.emailBuilder = emailBuilder;
    }

    @Override
    public void execute(UserCreated event) {

        final var user = event.getPayload().user();
        final var userId = user.id();

        final var builtData = emailBuilder.build(userId);

        emailService.send(
            user.email(),
            new EmailData(
                    builtData.subject(),
                    builtData.body()
            )
        );
    }
}
