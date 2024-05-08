package com.azat4dev.demobooking.users.users_commands.domain.values;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;

import java.util.UUID;

public record UserId(UUID value) {

    static UserId generateNew() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId fromString(String id) throws WrongFormatException {

        Assert.string(id, () -> new WrongFormatException(id))
            .notNull();

        try {
            return new UserId(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new WrongFormatException(id);
        }
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
