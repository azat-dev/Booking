package com.azat4dev.booking.shared.infrastructure.bus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class MessageSerializerJSON implements MessageSerializer<String> {

    private final ObjectMapper objectMapper;
    private final GetClassForMessageType getClassForMessageType;

    @Override
    public Object deserialize(String serializedMessage, String messageType) {
        final var dtoClass = getClassForMessageType.run(messageType);
        if (dtoClass == null) {
            log.atError()
                .addArgument(messageType)
                .log("Can't find dto class for: messageType={}");

            throw new Exception.FailedDeserialize(
                new RuntimeException("Can't find dto class for: messageType=" + messageType)
            );
        }

        try {
            return objectMapper.readValue(serializedMessage, dtoClass);
        } catch (JsonProcessingException e) {
            throw new Exception.FailedDeserialize(e);
        }
    }

    @Override
    public <M> String serialize(M message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new Exception.FailedSerialize(e);
        }
    }

    @FunctionalInterface
    public interface GetClassForMessageType {
        Class<?> run(String eventType);
    }
}
