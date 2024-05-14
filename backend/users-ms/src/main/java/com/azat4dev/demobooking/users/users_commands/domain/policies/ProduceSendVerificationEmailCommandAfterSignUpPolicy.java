package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.domain.Policy;
import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ProduceSendVerificationEmailCommandAfterSignUpPolicy implements Policy<DomainEventNew<UserCreated>> {

    private final DomainEventsBus bus;

    @Override
    public void execute(DomainEventNew<UserCreated> event) {

        final var payload = event.payload();
        if (payload.emailVerificationStatus() != EmailVerificationStatus.NOT_VERIFIED) {
            return;
        }

        final var command = new SendVerificationEmail(
            payload.userId(),
            payload.email(),
            payload.fullName(),
            0
        );

        bus.publish(command);
    }
}
