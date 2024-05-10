package com.azat4dev.demobooking.users.users_commands.data.repositories;

import com.azat4dev.demobooking.common.DomainEvent;
import com.azat4dev.demobooking.common.EventId;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FirstName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.entities.LastName;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreated;
import com.azat4dev.demobooking.users.users_commands.domain.events.UserCreatedPayload;
import com.azat4dev.demobooking.users.users_commands.domain.values.email.EmailAddress;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class DomainEventSerializerImpl implements DomainEventSerializer {

    private final ObjectMapper objectMapper;

    public DomainEventSerializerImpl() {
        this.objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();

        addValue(module, LocalDateTime.class, LocalDateTime::toString, LocalDateTime::parse);
        addValue(module, FirstName.class, FirstName::getValue, FirstName::dangerMakeFromStringWithoutCheck);
        addValue(module, LastName.class, LastName::getValue, LastName::dangerMakeFromStringWithoutCheck);
        addValue(module, EmailAddress.class, EmailAddress::getValue, EmailAddress::dangerMakeWithoutChecks);
        addValue(module, UserId.class, (v) -> v.value().toString(), UserId::fromString);
        addValue(module, EventId.class, (v) -> v.getValue(), EventId::dangerouslyCreateFrom);

        module.addDeserializer(UserCreated.class, new CustomClassDeserializer(UserCreated.class) {
            @Override
            Class<?> getClassForProperty(String propertyName) {
                return switch (propertyName) {
                    case "id" -> EventId.class;
                    case "timestampMs" -> Long.class;
                    case "payload" -> UserCreatedPayload.class;
                    case "createdAt" -> LocalDateTime.class;
                    case "type" -> null;
                    case "version" -> null;
                    default -> throw new RuntimeException("Unexpected property: " + propertyName);
                };
            }

            @Override
            Object createInstance(Map values) {
                return new UserCreated(
                    (EventId) values.get("id"),
                    (Long) values.get("timestampMs"),
                    (UserCreatedPayload) values.get("payload")
                );
            }
        });


        module.addDeserializer(FullName.class, new CustomClassDeserializer(FullName.class) {
            @Override
            Class<?> getClassForProperty(String propertyName) {
                return switch (propertyName) {
                    case "firstName" -> FirstName.class;
                    case "lastName" -> LastName.class;
                    default -> null;
                };
            }

            @Override
            Object createInstance(Map values) {
                return new FullName(
                    (FirstName) values.get("firstName"),
                    (LastName) values.get("lastName")
                );
            }
        });

        objectMapper.registerModule(module);
    }

    private static <T> void addValue(SimpleModule module, Class<T> clazz, Function<T, String> valueSupplier, Function<String, T> parseValue) {
        module.addSerializer(new StringValue<T>(clazz) {
            @Override
            public String getValue(T object) {
                return valueSupplier.apply(object);
            }
        });

        module.addDeserializer(clazz, new StringValueDeserializer<T>(clazz) {
            @Override
            T getValue(String value) {
                return parseValue.apply(value);
            }
        });
    }

    @Override
    public String serialize(DomainEvent<?> event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <Payload extends Serializable, Event extends DomainEvent<Payload>> DomainEvent<Payload> deserialize(String event, Class<Event> eventClass) {
        try {
            return this.objectMapper.readValue(event, eventClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class StringValue<T> extends ToStringSerializerBase {

        public StringValue(Class<T> type) {
            super(type);
        }

        @Override
        public String valueToString(Object value) {
            return getValue((T) value);
        }

        public String getValue(T object) {
            return object.toString();
        }
    }

    private abstract static class StringValueDeserializer<T> extends StdScalarDeserializer<T> {

        private final StringDeserializer stringDeserializer = new StringDeserializer();

        public StringValueDeserializer(Class<T> claszz) {
            super(claszz);
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            final var stringValue = stringDeserializer.deserialize(p, ctxt);

            if (stringValue == null) {
                return null;
            }

            return getValue(stringValue);
        }

        abstract T getValue(String value);
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
                        final var propertyName = p.getCurrentName();
                        final var classForProperty = getClassForProperty(propertyName);
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

        abstract Class<?> getClassForProperty(String propertyName);

        abstract T createInstance(Map<String, Object> values);
    }
}
