package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEventNew;
import com.azat4dev.demobooking.common.DomainEventPayload;
import com.azat4dev.demobooking.common.EventId;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.SendVerificationEmail;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.core.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    public DomainEventSerializerImpl() {

        final var objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();

        module.addDeserializer(DomainEventDTO.class, new CustomClassDeserializer(DomainEventDTO.class) {

            @Override
            Class<?> getClassForProperty(String propertyName, Map parsedValues) {

                final var eventType = (String) parsedValues.get("type");

                switch (propertyName) {
                    case "payload" -> {
                        return switch (eventType) {
                            case "UserCreated" -> UserCreatedDTO.class;
                            case "SendVerificationEmail" -> SendVerificationEmailDTO.class;
                            default -> throw new RuntimeException("Unexpected domain event type: " + eventType);
                        };
                    }
                    default -> {
                        try {
                            return this._valueClass.getDeclaredField(propertyName).getType();
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            @Override
            Object createInstance(Map values) {
                final var eventType = (String) values.get("type");
                final var payload = (Serializable) values.get("payload");

                return new DomainEventDTO(
                    (String) values.get("id"),
                    eventType,
                    (LocalDateTime) values.get("issuedAt"),
                    payload
                );
            }
        });

        objectMapper.registerModule(module);

        this.objectMapper = objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true)
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, true);
    }

    @Override
    public String serialize(DomainEventNew<?> event) {
        try {
            return objectMapper.writeValueAsString(DomainEventDTO.makeFrom(event));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DomainEventNew<?> deserialize(String event) {
        try {
            final var dto = this.objectMapper.readValue(event, DomainEventDTO.class);
            return dto.toDomain();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static abstract class CustomClassDeserializer<T> extends StdDeserializer<T> {

        protected CustomClassDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

            final var values = new HashMap<String, Object>();

            while (!p.isClosed()) {

                final var token = p.nextToken();
                if (token == null || token == JsonToken.END_OBJECT) {
                    break;
                }

                switch (token) {
                    case FIELD_NAME:
                        break;
                    case VALUE_NUMBER_INT:
                    case VALUE_STRING:
                    case START_OBJECT:
                    case START_ARRAY:
                        final var propertyName = p.getCurrentName();
                        final var classForProperty = getClassForProperty(propertyName, values);
                        if (classForProperty == null) {
                            break;
                        }

                        final var value = p.readValueAs(classForProperty);
                        values.put(propertyName, value);
                        break;
                    default:
                        throw new RuntimeException("Unexpected token: " + token);
                }
            }

            return createInstance(values);
        }

        abstract Class<?> getClassForProperty(String propertyName, Map<String, Object> parsedValues);

        abstract T createInstance(Map<String, Object> values);
    }
}


record FullNameDTO(
    String firstName,
    String lastName
) implements Serializable {

    public FullNameDTO(FullName fullName) {
        this(fullName.getFirstName().getValue(), fullName.getLastName().getValue());
    }

    public FullName toDomain() {
        return new FullName(
            FirstName.dangerMakeFromStringWithoutCheck(firstName),
            LastName.checkAndMakeFromString(lastName)
        );
    }
}

record UserCreatedDTO(
    LocalDateTime createdAt,
    String userId,
    FullNameDTO fullName,
    String email,
    String emailVerificationStatus
) implements Serializable {

    public UserCreated toDomain() {
        return new UserCreated(
            createdAt,
            UserId.fromString(userId),
            fullName.toDomain(),
            EmailAddress.dangerMakeWithoutChecks(email),
            EmailVerificationStatus.valueOf(emailVerificationStatus)
        );
    }
}

record SendVerificationEmailDTO(
    String userId,
    String email,
    FullNameDTO fullName
) implements Serializable {

    public SendVerificationEmail toDomain() {
        return new SendVerificationEmail(
            UserId.fromString(userId),
            EmailAddress.dangerMakeWithoutChecks(email),
            fullName.toDomain()
        );
    }
}

record DomainEventDTO(
    String id,
    String type,
    LocalDateTime issuedAt,
    Serializable payload
) implements Serializable {

    private static Serializable serialize(DomainEventPayload payload) {

        switch (payload) {
            case UserCreated userCreatedPayload -> {
                return new UserCreatedDTO(
                    userCreatedPayload.createdAt(),
                    userCreatedPayload.userId().value().toString(),
                    new FullNameDTO(userCreatedPayload.fullName()),
                    userCreatedPayload.email().getValue(),
                    userCreatedPayload.emailVerificationStatus().name()
                );
            }

            case SendVerificationEmail sendVerificationEmail -> {
                return new SendVerificationEmailDTO(
                    sendVerificationEmail.userId().value().toString(),
                    sendVerificationEmail.email().getValue(),
                    new FullNameDTO(sendVerificationEmail.fullName())
                );
            }

            default ->
                throw new RuntimeException("Unexpected domain event payload type: " + payload.getClass().getName());
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

    private DomainEventPayload payloadToDomain() {
        return switch (type) {
            case "UserCreated" -> ((UserCreatedDTO) payload).toDomain();
            case "SendVerificationEmail" -> ((SendVerificationEmailDTO) payload).toDomain();
            default -> throw new RuntimeException("Unexpected domain event type: " + type);
        };
    }

    public DomainEventNew<?> toDomain() {
        return new DomainEventNew<>(
            EventId.dangerouslyCreateFrom(id),
            issuedAt,
            payloadToDomain()
        );
    }
}