package com.azat4dev.booking.users.users_commands.data.repositories;

import com.azat4dev.booking.generated.events.dto.*;
import com.azat4dev.booking.shared.data.serializers.DomainEventSerializer;
import com.azat4dev.booking.shared.domain.events.DomainEvent;
import com.azat4dev.booking.shared.domain.events.DomainEventPayload;
import com.azat4dev.booking.shared.domain.events.EventId;
import com.azat4dev.booking.shared.domain.values.IdempotentOperationId;
import com.azat4dev.booking.shared.domain.values.files.*;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.booking.users.users_commands.domain.core.entities.UserPhotoPath;
import com.azat4dev.booking.users.users_commands.domain.core.events.*;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.FirstName;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.FullName;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.LastName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


@AllArgsConstructor
public final class DomainEventsSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    private static FullNameDTO map(FullName inst) {
        return FullNameDTO.builder()
            .firstName(inst.getFirstName().getValue())
            .lastName(inst.getLastName().getValue())
            .build();
    }

    @Override
    public String serialize(DomainEvent<?> event) {

        final var payload = toDTO(event.payload());
        final var dto = UsersDomainEventDTO.builder()
            .eventId(event.id().getValue())
            .occurredAt(map(event.issuedAt()))
            .payload(payload)
            .build();

        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public long map(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public LocalDateTime map(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    @Override
    public DomainEvent<?> deserialize(String event) {

        try {
            final var dto = objectMapper.readValue(event, UsersDomainEventDTO.class);
            final var payload = toDomain(dto.getPayload());
            return new DomainEvent<>(
                EventId.dangerouslyCreateFrom(dto.getEventId()),
                map(dto.getOccurredAt()),
                payload
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private UploadedFileData toDomain(UploadedFileDataDTO dto) {
        return new UploadedFileData(
            BucketName.makeWithoutChecks(dto.getBucketName()),
            MediaObjectName.dangerouslyMake(dto.getObjectName())
        );
    }

    private FullName toDomain(FullNameDTO fullNameDTO) {
        try {
            return new FullName(
                FirstName.dangerMakeFromStringWithoutCheck(fullNameDTO.getFirstName()),
                LastName.dangerMakeFromStringWithoutCheck(fullNameDTO.getLastName())
            );
        } catch (FullName.Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserPhotoPath toDomain(UserPhotoPathDTO dto) {
        return new UserPhotoPath(
            BucketName.makeWithoutChecks(dto.getBucketName()),
            MediaObjectName.dangerouslyMake(dto.getObjectName())
        );
    }

    private DomainEventPayload toDomain(UsersDomainEventPayloadDTO dto) {

        if (dto instanceof SendVerificationEmailDTO inst) {
            final var fullName = inst.getFullName();
            return new SendVerificationEmail(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                EmailAddress.dangerMakeWithoutChecks(inst.getEmail()),
                toDomain(fullName),
                inst.getAttempt()
            );
        }

        if (dto instanceof VerificationEmailSentDTO inst) {
            return new VerificationEmailSent(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                EmailAddress.dangerMakeWithoutChecks(inst.getEmail())
            );
        }

        if (dto instanceof UserVerifiedEmailDTO inst) {
            return new UserVerifiedEmail(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                EmailAddress.dangerMakeWithoutChecks(inst.getEmail())
            );
        }

        if (dto instanceof FailedToSendVerificationEmailDTO inst) {
            return new FailedToSendVerificationEmail(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                EmailAddress.dangerMakeWithoutChecks(inst.getEmail()),
                inst.getAttempts()
            );
        }

        if (dto instanceof UpdatedUserPhotoDTO inst) {
            return new UpdatedUserPhoto(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                toDomain(inst.getNewPhotoPath()),
                Optional.ofNullable(inst.getPrevPhotoPath()).map(this::toDomain)

            );
        }

        if (dto instanceof UserCreatedDTO inst) {
            return new UserCreated(
                map(inst.getCreatedAt()),
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                toDomain(inst.getFullName()),
                EmailAddress.dangerMakeWithoutChecks(inst.getEmail()),
                EmailVerificationStatus.valueOf(inst.getEmailVerificationStatus())
            );
        }

        if (dto instanceof UserDidResetPasswordDTO inst) {
            return new UserDidResetPassword(
                UserId.dangerouslyMakeFrom(inst.getUserId())
            );
        }

        if (dto instanceof GeneratedUserPhotoUploadUrlDTO inst) {
            return new GeneratedUserPhotoUploadUrl(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                toDomain(inst.getFormData())
            );
        }

        if (dto instanceof FailedGenerateUserPhotoUploadUrlDTO inst) {
            return new FailedGenerateUserPhotoUploadUrl(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                PhotoFileExtension.dangerouslyMakeFrom(inst.getFileExtension()),
                inst.getFileSize(),
                IdempotentOperationId.dangerouslyMakeFrom(inst.getOperationId())
            );
        }

        if (dto instanceof FailedUpdateUserPhotoDTO inst) {
            return new FailedUpdateUserPhoto(
                IdempotentOperationId.dangerouslyMakeFrom(inst.getOperationId()),
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                toDomain(inst.getUploadedFileData())
            );
        }

        if (dto instanceof SentEmailForPasswordResetDTO inst) {
            return new SentEmailForPasswordReset(
                UserId.dangerouslyMakeFrom(inst.getUserId()),
                EmailAddress.dangerMakeWithoutChecks(inst.getEmail())
            );
        }

        throw new RuntimeException("Deserialization. Unknown event type: " + dto);
    }

    private UserPhotoPathDTO toDTO(UserPhotoPath dm) {
        return UserPhotoPathDTO.builder()
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .build();
    }

    private UploadedFileFormDataDTO toDTO(UploadFileFormData dm) {

        return UploadedFileFormDataDTO.builder()
            .url(URI.create(dm.url().toString()))
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .formData(dm.formData())
            .build();
    }

    private UploadFileFormData toDomain(UploadedFileFormDataDTO dto) {
        try {
            return new UploadFileFormData(
                dto.getUrl().toURL(),
                BucketName.makeWithoutChecks(dto.getBucketName()),
                MediaObjectName.dangerouslyMake(dto.getObjectName()),
                dto.getFormData()
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private UsersDomainEventPayloadDTO toDTO(DomainEventPayload payload) {

        return switch (payload) {
            case SendVerificationEmail inst -> SendVerificationEmailDTO.builder()
                .userId(inst.userId().toString())
                .email(inst.email().getValue())
                .fullName(map(inst.fullName()))
                .attempt(inst.attempt())
                .build();

            case VerificationEmailSent inst -> VerificationEmailSentDTO.builder()
                .userId(inst.userId().toString())
                .email(inst.emailAddress().getValue())
                .build();

            case UserVerifiedEmail inst -> UserVerifiedEmailDTO.builder()
                .userId(inst.userId().toString())
                .email(inst.emailAddress().getValue())
                .build();

            case FailedToSendVerificationEmail inst -> FailedToSendVerificationEmailDTO.builder()
                .userId(inst.userId().toString())
                .email(inst.email().getValue())
                .attempts(inst.attempts())
                .build();

            case UpdatedUserPhoto inst -> UpdatedUserPhotoDTO.builder()
                .userId(inst.userId().toString())
                .newPhotoPath(toDTO(inst.newPhotoPath()))
                .prevPhotoPath(inst.prevPhotoPath().map(this::toDTO).orElse(null))
                .build();

            case UserCreated inst -> UserCreatedDTO.builder()
                .createdAt(map(inst.createdAt()))
                .userId(inst.userId().toString())
                .email(inst.email().getValue())
                .fullName(map(inst.fullName()))
                .emailVerificationStatus(inst.emailVerificationStatus().name())
                .build();

            case UserDidResetPassword inst -> UserDidResetPasswordDTO.builder()
                .userId(inst.userId().toString())
                .build();

            case GeneratedUserPhotoUploadUrl inst -> GeneratedUserPhotoUploadUrlDTO.builder()
                .userId(inst.userId().toString())
                .formData(toDTO(inst.formData()))
                .build();

            case FailedGenerateUserPhotoUploadUrl inst -> FailedGenerateUserPhotoUploadUrlDTO.builder()
                .userId(inst.userId().toString())
                .operationId(inst.operationId().toString())
                .fileExtension(inst.fileExtension().toString())
                .fileSize(inst.fileSize())
                .build();

            case FailedUpdateUserPhoto inst -> FailedUpdateUserPhotoDTO.builder()
                .operationId(inst.operationId().toString())
                .userId(inst.userId().toString())
                .uploadedFileData(toDTO(inst.uploadedFileData()))
                .build();

            case SentEmailForPasswordReset inst -> SentEmailForPasswordResetDTO.builder()
                .userId(inst.userId().toString())
                .email(inst.email().getValue())
                .build();

            default -> throw new RuntimeException("Serialization. Unknown event type: " + payload);
        };
    }

    private UploadedFileDataDTO toDTO(UploadedFileData dm) {
        return UploadedFileDataDTO.builder()
            .bucketName(dm.bucketName().getValue())
            .objectName(dm.objectName().getValue())
            .build();
    }
}
