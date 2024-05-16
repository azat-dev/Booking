package com.azat4dev.demobooking.users.users_commands.data.repositories.dto;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.common.domain.event.EventId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.*;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

public record DomainEventDTO(
    String id,
    LocalDateTime issuedAt,
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
    )
    @JsonTypeIdResolver(DomainEventTypeResolver.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = UserCreatedDTO.class),
        @JsonSubTypes.Type(value = SendVerificationEmailDTO.class),
        @JsonSubTypes.Type(value = VerificationEmailSentDTO.class),
        @JsonSubTypes.Type(value = FailedToSendVerificationEmailDTO.class),
        @JsonSubTypes.Type(value = CompleteEmailVerificationDTO.class),
        @JsonSubTypes.Type(value = SentEmailForPasswordResetDTO.class),
        @JsonSubTypes.Type(value = ResetPasswordByEmailDTO.class),
        @JsonSubTypes.Type(value = UserDidResetPasswordDTO.class),
        @JsonSubTypes.Type(value = CompletePasswordResetDTO.class),
        @JsonSubTypes.Type(value = GenerateUserPhotoUploadUrlDTO.class),
        @JsonSubTypes.Type(value = GeneratedUserPhotoUploadUrlDTO.class),
        @JsonSubTypes.Type(value = FailedGenerateUserPhotoUploadUrlDTO.class),
    })
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

            case SentEmailForPasswordReset inst -> {
                return SentEmailForPasswordResetDTO.fromDomain(inst);
            }

            case ResetPasswordByEmail inst -> {
                return ResetPasswordByEmailDTO.fromDomain(inst);
            }

            case UserDidResetPassword inst -> {
                return UserDidResetPasswordDTO.fromDomain(inst);
            }

            case CompletePasswordReset inst -> {
                return CompletePasswordResetDTO.fromDomain(inst);
            }

            case GeneratedUserPhotoUploadUrl inst -> {
                return GeneratedUserPhotoUploadUrlDTO.fromDomain(inst);
            }

            case GenerateUserPhotoUploadUrl inst -> {
                return GenerateUserPhotoUploadUrlDTO.fromDomain(inst);
            }

            case FailedGenerateUserPhotoUploadUrl inst -> {
                return FailedGenerateUserPhotoUploadUrlDTO.fromDomain(inst);
            }

            default ->
                throw new RuntimeException("Can't serialize. Unexpected domain event payload type: " + payload.getClass().getSimpleName());
        }
    }

    public static DomainEventDTO makeFrom(DomainEventNew event) {
        return new DomainEventDTO(
            event.id().getValue(),
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

class DomainEventTypeResolver extends TypeIdResolverBase {

    private final PolymorphicTypeValidator validator = new DefaultBaseTypeLimitingValidator();

    private JavaType baseType;

    @Override
    public void init(JavaType baseType) {
        this.baseType = baseType;
    }

    @Override
    public String idFromValue(Object value) {
        return idFromValueAndType(value, value.getClass());
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        final var clazz = suggestedType.getSimpleName();
        return clazz.substring(0, clazz.length() - 3);
    }

    @Override
    public String idFromBaseType() {
        return idFromValueAndType(null, this._baseType.getRawClass());
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {

        final var fullClassName = this.getClass().getPackageName() + "." + id + "DTO";

        JavaType t = context.resolveAndValidateSubType(baseType, fullClassName, validator);

        if (t == null) {
            if (context instanceof DeserializationContext) {
                // First: we may have problem handlers that can deal with it?
                return ((DeserializationContext) context).handleUnknownTypeId(baseType, fullClassName, this, "no such class found");
            }
            // ... meaning that we really should never get here.
        }
        return t;
    }


    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}