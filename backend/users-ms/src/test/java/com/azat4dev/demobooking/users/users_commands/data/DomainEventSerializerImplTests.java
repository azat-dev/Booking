package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.common.domain.event.RandomEventIdGenerator;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializerImpl;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.VerificationEmailSent;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.azat4dev.demobooking.users.users_commands.domain.EventHelpers.eventsFactory;
import static com.azat4dev.demobooking.users.users_commands.domain.UserHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;



public class DomainEventSerializerImplTests {

    DomainEventSerializer createSUT() {
        return new DomainEventSerializerImpl();
    }

    EventId anyEventId() {
        return new RandomEventIdGenerator().generate();
    }

    @Test
    void test_serialize() {

        final var events = List.of(
            eventsFactory.issue(
                new UserCreated(
                    LocalDateTime.now(),
                    anyValidUserId(),
                    anyFullName(),
                    anyValidEmail(),
                    EmailVerificationStatus.NOT_VERIFIED
                )
            ),
            eventsFactory.issue(
                new SendVerificationEmail(
                    anyValidUserId(),
                    anyValidEmail(),
                    anyFullName(),
                    5
                )
            ),
            eventsFactory.issue(
                new UserVerifiedEmail(
                    anyValidUserId(),
                    anyValidEmail()
                )
            ),
            eventsFactory.issue(
                new VerificationEmailSent(
                    anyValidUserId(),
                    anyValidEmail()
                )
            ),
            eventsFactory.issue(
                new FailedToSendVerificationEmail(
                    anyValidUserId(),
                    anyValidEmail(),
                    5
                )
            ),
            eventsFactory.issue(
                new CompleteEmailVerification(
                    new EmailVerificationToken("token")
                )
            )
        );

        for (final var event : events) {
            // Given
            final var sut = createSUT();

            // When
            final var serialized = sut.serialize(event);
            final var deserializedValue = sut.deserialize(serialized);

            // Then
            assertThat(deserializedValue).isEqualTo(event);
        }
    }
}
