package com.azat4dev.demobooking.common.domain.core;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

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
