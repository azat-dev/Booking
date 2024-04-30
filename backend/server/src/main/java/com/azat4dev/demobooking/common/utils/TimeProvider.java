package com.azat4dev.demobooking.common.utils;

import org.springframework.lang.NonNull;

import java.util.Date;

public interface TimeProvider {
    Date currentTime();
}