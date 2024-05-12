package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventPayload;
import com.azat4dev.demobooking.common.EventId;
import com.azat4dev.demobooking.common.RandomEventIdGenerator;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializerImpl;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

        // Given
        final var sut = createSUT();
        final var event = new DomainEventNew<DomainEventPayload>(
            anyEventId(),
            LocalDateTime.now(),
            new UserCreated(
                LocalDateTime.now(),
                anyValidUserId(),
                anyFullName(),
                anyValidEmail(),
                EmailVerificationStatus.NOT_VERIFIED
            )
        );

        // When
        final var serialized = sut.serialize(event);
        final var deserializedValue = sut.deserialize(serialized);

        // Then
        assertThat(deserializedValue).isEqualTo(event);
    }
}
