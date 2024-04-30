package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.common.DomainDataFormatException;

public class LastNameMaxLengthException extends DomainDataFormatException {
    public LastNameMaxLengthException(String value) {
        super("The last name is too long. Max length is " + LastName.MAX_LENGTH + " characters. Value is " + value);
    }
}
