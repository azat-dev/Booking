package com.azat4dev.booking.shared.utils;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime currentTime();
}