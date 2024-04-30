package com.azat4dev.demobooking.common.utils;

import org.springframework.lang.NonNull;

import java.util.Date;

public final class SystemTimeProvider implements TimeProvider {

    @Override
    public Date currentTime() {
        return new Date();
    }
}
