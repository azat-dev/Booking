package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.common.DomainEventsFactory;
import com.azat4dev.demobooking.users.users_commands.domain.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendVerificationEmailPolicyImpl implements SendVerificationEmailPolicy {

    private final DomainEventsBus bus;
    private final DomainEventsFactory domainEventsFactory;

    @Override
    public void execute(DomainEventNew<UserCreated> event) {

        final var payload = event.payload();
        if (payload.emailVerificationStatus() != EmailVerificationStatus.NOT_VERIFIED) {
            return;
        }

        final var command = domainEventsFactory.issue(
            new SendVerificationEmail(
                payload.userId(),
                payload.email(),
                payload.fullName()
            )
        );
        bus.publish(command);
    }
}
