package com.azat4dev.demobooking.users.domain.entities;

import com.azat4dev.demobooking.common.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;

import java.util.Objects;

public final class FullName {

    private final FirstName firstName;
    private final LastName lastName;

    public FullName(FirstName firstName, LastName lastName) throws LastNameCantBeNullException, FirstNameCantBeNullException {
        Assert.notNull(firstName, FirstNameCantBeNullException::new);
        Assert.notNull(lastName, LastNameCantBeNullException::new);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public FirstName firstName() {
        return firstName;
    }

    public LastName lastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FullName) obj;
        return Objects.equals(this.firstName, that.firstName) &&
            Objects.equals(this.lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public static class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }

        @Override
        public String getCode() {
            return "InvalidFullName";
        }
    }

    public static class FirstNameCantBeNullException extends ValidationException {
        public FirstNameCantBeNullException() {
            super("The first name cannot be null");
        }

        @Override
        public String getCode() {
            return "FirstNameIsNull";
        }
    }

    public static class LastNameCantBeNullException extends ValidationException {
        public LastNameCantBeNullException() {
            super("The last name cannot be null");
        }

        @Override
        public String getCode() {
            return "LastNameIsNull";
        }
    }
}
