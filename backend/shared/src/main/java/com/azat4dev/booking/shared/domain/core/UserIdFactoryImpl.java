package com.azat4dev.booking.shared.domain.core;

import com.azat4dev.booking.shared.domain.core.UserId;

import java.util.UUID;

public class UserIdFactoryImpl implements UserIdFactory {

    @Override
    public UserId generateNewUserId() {
        try {
            return UserId.fromUUID(UUID.randomUUID());
        } catch (UserId.WrongFormatException e) {
            throw new RuntimeException(e);
        }
    }
}