package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.common.DomainDataFormatException;

public class FirstNameCantBeEmptyException extends DomainDataFormatException {

    public FirstNameCantBeEmptyException() {
        super("First name can not be empty");
    }
}
