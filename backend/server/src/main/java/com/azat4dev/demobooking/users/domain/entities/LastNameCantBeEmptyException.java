package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.common.DomainDataFormatException;

public class LastNameCantBeEmptyException extends DomainDataFormatException {
    public LastNameCantBeEmptyException() {
        super("The last name cannot be empty");
    }
}
