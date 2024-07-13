package com.azat4dev.booking.shared.data.tracing;

import javax.annotation.Nullable;
import java.util.Map;

public interface ParseTracingInfo {

    @Nullable
    Map<String, String> execute(@Nullable String tracingInfo) throws Exception;
}
