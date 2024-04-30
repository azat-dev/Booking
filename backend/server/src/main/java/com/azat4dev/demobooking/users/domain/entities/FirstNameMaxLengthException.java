package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.common.DomainDataFormatException;

public class FirstNameMaxLengthException extends DomainDataFormatException {
    public FirstNameMaxLengthException(String value) {
        super("The first name is too long. Max length is " + FirstName.MAX_LENGTH + " characters. Value is " + value);
    }
}
