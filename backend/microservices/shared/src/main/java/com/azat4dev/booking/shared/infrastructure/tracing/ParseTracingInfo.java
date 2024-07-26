package com.azat4dev.booking.shared.infrastructure.tracing;

import javax.annotation.Nullable;
import java.util.Map;

public interface ParseTracingInfo {

    @Nullable
    Map<String, String> execute(@Nullable String tracingInfo) throws Exception;
}
