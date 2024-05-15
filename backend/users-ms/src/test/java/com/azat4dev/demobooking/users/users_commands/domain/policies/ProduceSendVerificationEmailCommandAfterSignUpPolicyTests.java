package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventsBus;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.common.domain.event.RandomEventIdGenerator;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class ProduceSendVerificationEmailCommandAfterSignUpPolicyTests {

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
    void test_givenUserCreated_thenEmitSendVerificationEmailCommand() {

        // Given
        final var sut = createSUT();
        final var email = UserHelpers.anyValidEmail();
        final var userId = UserHelpers.anyValidUserId();
        final var fullName = UserHelpers.anyFullName();

        final var inputEvent = new DomainEventNew<>(
            anyEventId(),
            LocalDateTime.now(),
            new UserCreated(
                LocalDateTime.now(),
                userId,
                fullName,
                email,
                EmailVerificationStatus.NOT_VERIFIED
            )
        );

        final var expectedOutput = new SendVerificationEmail(
            userId,
            email,
            fullName,
            0
        );
        // When
        sut.policy.execute(inputEvent);

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
