package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.domain.event.DomainEventNew;
import com.azat4dev.demobooking.users.users_commands.data.repositories.dto.*;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    private final Map<String, ? extends Class<? extends Record>> payloadClassesByEventTypes = Map.of(
        "UserCreated", UserCreatedDTO.class,
        "SendVerificationEmail", SendVerificationEmailDTO.class,
        "VerificationEmailSent", VerificationEmailSentDTO.class,
        "FailedToSendVerificationEmail", FailedToSendVerificationEmailDTO.class,
        "UserVerifiedEmail", UserVerifiedEmailDTO.class,
        "CompleteEmailVerification", CompleteEmailVerificationDTO.class,
        "ResetPasswordByEmail", ResetPasswordByEmailDTO.class,
        "UserDidResetPassword", UserDidResetPasswordDTO.class,
        "CompletePasswordReset", CompletePasswordResetDTO.class
    );

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
                        return payloadClassesByEventTypes.get(eventType);
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
                final var payload = (DomainEventPayloadDTO) values.get("payload");

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

