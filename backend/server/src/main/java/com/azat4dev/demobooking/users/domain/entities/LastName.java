package com.azat4dev.demobooking.users.domain.entities;

import java.util.Objects;

public final class LastName {

    public final static int MAX_LENGTH = 255;

    private final String value;

    public LastName(String value) throws LastNameCantBeEmptyException, LastNameMaxLengthException {
        if (value.isEmpty()) {
            throw new LastNameCantBeEmptyException();
        }

        if (value.length() > MAX_LENGTH) {
            throw new LastNameMaxLengthException(value);
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LastName lastName)) return false;
        return Objects.equals(value, lastName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
