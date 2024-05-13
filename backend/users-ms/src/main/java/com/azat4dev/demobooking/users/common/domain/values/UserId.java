package com.azat4dev.demobooking.users.common.domain.values;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;

import java.util.UUID;

public record UserId(UUID value) {

    public static UserId fromString(String id) throws WrongFormatException {

        Assert.notNull(id, () -> new WrongFormatException(id));

        try {
            return new UserId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new WrongFormatException(id);
        }
    }

    public static UserId fromUUID(UUID id) throws WrongFormatException {
        Assert.notNull(id, () -> new WrongFormatException("null"));
        return new UserId(id);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static class WrongFormatException extends DomainException {
        public WrongFormatException(String value) {
            super("Wrong formatted user id. Value: " + value);
        }

        @Override
        public String getCode() {
            return "WrongFormat";
        }
    }
}
