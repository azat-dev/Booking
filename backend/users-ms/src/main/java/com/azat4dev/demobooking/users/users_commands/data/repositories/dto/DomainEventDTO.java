package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.FailedToSendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserVerifiedEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.VerificationEmailSent;

import java.io.Serializable;
import java.time.LocalDateTime;

public record DomainEventDTO(
    String id,
    String type,
    LocalDateTime issuedAt,
    DomainEventPayloadDTO payload
) implements Serializable {

    private static DomainEventPayloadDTO serialize(DomainEventPayload payload) {

        switch (payload) {
            case UserCreated inst -> {
                return UserCreatedDTO.fromDomain(inst);
            }
            case SendVerificationEmail inst -> {
                return SendVerificationEmailDTO.fromDomain(inst);
            }
            case VerificationEmailSent inst -> {
                return VerificationEmailSentDTO.fromDomain(inst);
            }
            case FailedToSendVerificationEmail inst -> {
                return FailedToSendVerificationEmailDTO.fromDomain(inst);
            }
            case UserVerifiedEmail inst -> {
                return UserVerifiedEmailDTO.fromDomain(inst);
            }
            case CompleteEmailVerification inst -> {
                return CompleteEmailVerificationDTO.fromDomain(inst);
            }

            default ->
                throw new RuntimeException("Can't serialize. Unexpected domain event payload type: " + payload.getClass().getSimpleName());
        }
    }

    public static DomainEventDTO makeFrom(DomainEventNew event) {
        return new DomainEventDTO(
            event.id().getValue(),
            event.payload().getClass().getSimpleName(),
            event.issuedAt(),
            serialize(event.payload())
        );
    }

    public DomainEventNew<?> toDomain() {
        return new DomainEventNew<>(
            EventId.dangerouslyCreateFrom(id),
            issuedAt,
            payload.toDomain()
        );
    }
}
