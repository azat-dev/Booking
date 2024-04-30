package com.azat4dev.demobooking.users.domain.entities;

import java.util.Objects;

public final class FirstName {

    public static final int MAX_LENGTH = 255;

    private final String value;

    public FirstName(String value) throws FirstNameCantBeEmptyException, FirstNameMaxLengthException {
        if (value.isEmpty()) {
            throw new FirstNameCantBeEmptyException();
        }

        if (value.length() > MAX_LENGTH) {
            throw new FirstNameMaxLengthException(value);
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FirstName) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
