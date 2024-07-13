package com.azat4dev.booking.shared.domain.interfaces.tracing;

import javax.annotation.Nullable;
import java.util.Map;

@FunctionalInterface
public interface ExecuteWithTraceContext {
    void run(@Nullable Map<String, String> traceInfo, Runnable action);
}