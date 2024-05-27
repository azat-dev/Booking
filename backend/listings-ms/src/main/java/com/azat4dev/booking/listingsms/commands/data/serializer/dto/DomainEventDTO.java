package com.azat4dev.booking.listingsms.commands.data.serializer.dto;

import com.azat4dev.booking.shared.data.DomainEventPayloadDTO;
import com.azat4dev.booking.shared.data.SerializableDomainEvent;
import com.azat4dev.booking.shared.domain.event.DomainEvent;
import com.azat4dev.booking.shared.domain.event.DomainEventPayload;
import com.azat4dev.booking.shared.domain.event.EventId;
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
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;

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
        @JsonSubTypes.Type(value = NewListingAddedDTO.class),
    })
    DomainEventPayloadDTO payload
) implements SerializableDomainEvent {

    private static DomainEventPayloadDTO serialize(DomainEventPayload payload) {

        final var payloadClass = payload.getClass().getSimpleName();
        try {
            final var subtypes = DomainEventDTO.class.getDeclaredField("payload").getAnnotation(JsonSubTypes.class).value();
            final var dtoClass = Arrays.stream(subtypes)
                .map(subtype -> subtype.value())
                .filter(subtype -> subtype.getSimpleName().equals(payloadClass + "DTO"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't serialize. Unexpected domain event payload type: " + payloadClass));

            final var fromDomainMethod = dtoClass.getDeclaredMethod("fromDomain", payload.getClass());
            assert fromDomainMethod != null;
            assert payload != null;
            return (DomainEventPayloadDTO) fromDomainMethod.invoke(null, payload);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static DomainEventDTO makeFrom(DomainEvent event) {
        return new DomainEventDTO(
            event.id().getValue(),
            event.issuedAt(),
            serialize(event.payload())
        );
    }

    public DomainEvent<?> toDomain() {
        return new DomainEvent<>(
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

        final var fullClassName = this.getClass().getPackageName() +
                                  "." +
                                  id +
                                  "DTO";

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