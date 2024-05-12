package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventsBus;
import com.azat4dev.demobooking.common.DomainEventsFactory;
import com.azat4dev.demobooking.common.Policy;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SendVerificationEmailAfterSignUpPolicy implements Policy<DomainEventNew<UserCreated>> {

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
