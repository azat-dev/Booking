package com.azat4dev.booking.shared.data.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class TypedSerializerJson implements TypedSerializer<String> {

    private final ObjectMapper objectMapper;

    @Override
    public <MESSAGE> String serialize(MESSAGE message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new Exception.FailedSerialize(e);
        }
    }

    @Override
    public <MESSAGE> MESSAGE deserialize(String message, Class<MESSAGE> type) {
        try {
            return objectMapper.readValue(message, type);
        } catch (JsonProcessingException e) {
            throw new Exception.FailedDeserialize(e);
        }
    }
}
