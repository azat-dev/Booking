package com.azat4dev.booking.shared.data.tracing.outbox;

import io.micrometer.observation.transport.SenderContext;

import java.util.Map;

public final class OutboxInputReceiverContext extends SenderContext<Map<String, String>> {

    private final Map<String, String> tracingInfo;

    public OutboxInputReceiverContext(Map<String, String> tracingInfo) {
        super((carrier, key, value) -> tracingInfo.put(key, value));
        this.tracingInfo = tracingInfo;
    }

    public Map<String, String> getTracingInfo() {
        return tracingInfo;
    }
}
