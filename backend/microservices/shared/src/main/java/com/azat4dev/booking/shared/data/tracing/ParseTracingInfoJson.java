package com.azat4dev.booking.shared.data.tracing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;

@AllArgsConstructor
public class ParseTracingInfoJson implements ParseTracingInfo {

    private final ObjectMapper objectMapper;

    @Override
    public @Nullable Map<String, String> execute(@Nullable String tracingInfo) throws Exception {
        if (tracingInfo == null || tracingInfo.isEmpty()) {
            return null;
        }

        return objectMapper.readValue(tracingInfo, Map.class);
    }
}
