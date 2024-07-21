package com.azat4dev.booking.users.config.users_commands.infrastructure;

import com.azat4dev.booking.shared.data.tracing.ExecuteWithTraceContextSpring;
import com.azat4dev.booking.shared.data.tracing.ExtractTraceContextSpring;
import com.azat4dev.booking.shared.data.tracing.ParseTracingInfo;
import com.azat4dev.booking.shared.data.tracing.ParseTracingInfoJson;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExecuteWithTraceContext;
import com.azat4dev.booking.shared.domain.interfaces.tracing.ExtractTraceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.ObservationRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class TracingConfig {

    private final ObservationRegistry observationRegistry;
    private final ObjectMapper objectMapper;

    @Bean
    ExtractTraceContext extractTraceContextInfo() {
        return new ExtractTraceContextSpring(objectMapper, observationRegistry);
    }

    @Bean
    ExecuteWithTraceContext executeWithTrace() {
        return new ExecuteWithTraceContextSpring(observationRegistry);
    }

    @Bean
    ParseTracingInfo parseTracingInfo() {
        return new ParseTracingInfoJson(objectMapper);
    }
}