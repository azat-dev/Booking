package com.azat4dev.demobooking.users.users_commands.domain.policies;

import com.azat4dev.demobooking.common.*;
import com.azat4dev.demobooking.common.utils.SystemTimeProvider;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;


public class SendVerificationEmailPolicyTests {

    SUT createSUT() {
        final var bus = mock(DomainEventsBus.class);

        return new SUT(
            new SendVerificationEmailPolicyImpl(
                bus,
                new DomainEventsFactoryImpl(
                    new RandomEventIdGenerator(),
                    new SystemTimeProvider()
                )
            ),
            bus
        );
    }

    EventId anyEventId() {
        return new RandomEventIdGenerator().generate();
    }

    @Test
    void given_valid_user__when_SendVerificationEmailPolicyImpl_triggered__thenEmitSendVerificationEmailCommand() {

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
            fullName
        );
        // When
        sut.policy.execute(inputEvent);

        // Then
        then(sut.bus)
            .should(times(1))
            .publish(assertArg(m -> {
                assertThat(m.payload()).isEqualTo(expectedOutput);
            }));
    }

    record SUT(
        SendVerificationEmailPolicy policy,
        DomainEventsBus bus
    ) {
    }
}
