package com.azat4dev.booking.users.users_commands.domain.policies;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ProduceSendVerificationEmailCommandAfterSignUpPolicy implements Policy<DomainEvent<UserSignedUp>> {

    private final DomainEventsBus bus;

    @Override
    public void execute(DomainEvent<UserSignedUp> event) {

        System.out.println("ProduceSendVerificationEmailCommandAfterSignUpPolicy send ");
        System.out.println(event);
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
