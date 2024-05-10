package com.azat4dev.demobooking.users.users_commands.data;

import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializer;
import com.azat4dev.demobooking.users.users_commands.data.repositories.DomainEventSerializerImpl;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.azat4dev.demobooking.users.users_commands.domain.UserHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DomainEventSerializerImplTests {

    DomainEventSerializer createSUT() {
        return new DomainEventSerializerImpl(new ObjectMapper());
    }

    @Test
    void test_serialize() {

        // Given
        final var sut = createSUT();
        final var event = new UserCreated(
            UUID.randomUUID().toString(),
            Instant.now().toEpochMilli(),
            new UserCreatedPayload(
                LocalDateTime.now(),
                anyValidUserId(),
                anyFullName(),
                anyValidEmail(),
                EmailVerificationStatus.NOT_VERIFIED
            )
        );

        // When
        final var serialized = sut.serialize(event);

        // Then
        assertThat(serialized).isNotNull();
    }
}
