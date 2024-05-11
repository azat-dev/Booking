package com.azat4dev.demobooking.common.utils;

import java.time.LocalDateTime;

public final class SystemTimeProvider implements TimeProvider {

    @Override
    public LocalDateTime currentTime() {
        return LocalDateTime.now();
    }
}
