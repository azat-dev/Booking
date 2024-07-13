package com.azat4dev.booking.shared.data.tracing.outbox;

import io.micrometer.observation.transport.ReceiverContext;

import java.util.Map;

public final class OutboxOutputReceiverContext extends ReceiverContext<Map<String, String>> {
    public OutboxOutputReceiverContext(Map<String, String> traceInfo) {
        super((carrier, key) -> {
            return traceInfo.get(key);
        });
    }
}
