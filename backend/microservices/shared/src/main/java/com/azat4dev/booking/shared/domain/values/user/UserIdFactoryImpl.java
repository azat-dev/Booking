package com.azat4dev.booking.shared.domain.values.user;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.UUID;

public class UserIdFactoryImpl implements UserIdFactory {

    @Override
    public UserId generateNewUserId() {
        try {
            return UserId.fromUUID(UUID.randomUUID());
        } catch (UserId.WrongFormatException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
