package com.azat4dev.booking.users.commands.domain.policies;

import com.azat4dev.booking.shared.domain.Policy;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Observed
@RequiredArgsConstructor
public class ProduceSendVerificationEmailCommandAfterSignUpPolicy implements Policy<UserSignedUp> {

    private final DomainEventsBus bus;

    @Override
    public void execute(UserSignedUp event, EventId eventId, LocalDateTime issuedAt) {
        if (event.emailVerificationStatus() != EmailVerificationStatus.NOT_VERIFIED) {
            return;
        }

        final var command = new SendVerificationEmail(
            event.userId(),
            event.email(),
            event.fullName(),
            0
        );

        bus.publish(command);
    }
}
