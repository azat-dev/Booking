package com.azat4dev.booking.shared.infrastructure.bus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MessageSerializerJSON implements MessageSerializer<String> {

    private final ObjectMapper objectMapper;
    private final GetDtoClassForMessageType getDtoClassForMessageType;

    @Override
    public Object deserialize(String serializedMessage, String messageType) throws Exception.FailedDeserialize {

        try {
            final var dtoClass = getDtoClassForMessageType.run(messageType);
            return objectMapper.readValue(serializedMessage, dtoClass);
        } catch (java.lang.Exception e) {
            throw new Exception.FailedDeserialize(e);
        }
    }

    @Override
    public <M> String serialize(M message) throws Exception.FailedSerialize {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new Exception.FailedSerialize(e);
        }
    }

    @FunctionalInterface
    public interface GetDtoClassForMessageType {

        @Nonnull
        Class<?> run(String messageType);
    }
}
