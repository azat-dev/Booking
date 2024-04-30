package com.azat4dev.demobooking.users.domain.values;

import com.azat4dev.demobooking.common.DomainDataFormatException;

public final class WrongPasswordFormatException extends DomainDataFormatException {
    public WrongPasswordFormatException() {
        super("Wrong password format");
    }
}
