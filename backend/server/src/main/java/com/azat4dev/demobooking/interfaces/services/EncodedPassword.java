package com.azat4dev.demobooking.interfaces.services;

public final class EncodedPassword {

    private final String value;

    public EncodedPassword(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EncodedPassword other)) {
            return false;
        }

        return this.value.equals(other.getValue());
    }
}