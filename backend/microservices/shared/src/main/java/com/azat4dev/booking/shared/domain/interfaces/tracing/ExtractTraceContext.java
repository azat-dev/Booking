package com.azat4dev.booking.shared.domain.interfaces.tracing;


import javax.annotation.Nullable;

@FunctionalInterface
public interface ExtractTraceContext {
    @Nullable
    String execute();
}