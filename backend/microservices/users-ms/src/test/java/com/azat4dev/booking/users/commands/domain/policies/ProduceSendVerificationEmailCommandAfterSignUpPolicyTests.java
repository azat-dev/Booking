package com.azat4dev.booking.users.commands.domain.policies;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.events.RandomEventIdGenerator;
import com.azat4dev.booking.users.commands.domain.UserHelpers;
import com.azat4dev.booking.users.commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.commands.domain.core.events.UserSignedUp;
import com.azat4dev.booking.users.commands.domain.core.values.user.EmailVerificationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


class ProduceSendVerificationEmailCommandAfterSignUpPolicyTests {

    SUT createSUT() {
        final var bus = mock(DomainEventsBus.class);

        return new SUT(
            new ProduceSendVerificationEmailCommandAfterSignUpPolicy(bus),
            bus
        );
    }

    EventId anyEventId() {
        return new RandomEventIdGenerator().generate();
    }

    @Test
    void test_givenUserSignedUp_thenEmitSendVerificationEmailCommand() {

        // Given
        final var sut = createSUT();
        final var email = UserHelpers.anyValidEmail();
        final var userId = UserHelpers.anyValidUserId();
        final var fullName = UserHelpers.anyFullName();

        final var eventId = anyEventId();
        final var issuedAt = LocalDateTime.now();

        final var event = new UserSignedUp(
            LocalDateTime.now(),
            userId,
            fullName,
            email,
            EmailVerificationStatus.NOT_VERIFIED
        );

        final var expectedOutput = new SendVerificationEmail(
            userId,
            email,
            fullName,
            0
        );
        // When
        sut.policy.execute(event, eventId, issuedAt);

        // Then
        then(sut.bus)
            .should(times(1))
            .publish(expectedOutput);
    }

    record SUT(
        ProduceSendVerificationEmailCommandAfterSignUpPolicy policy,
        DomainEventsBus bus
    ) {
    }
}
