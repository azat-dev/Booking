package com.azat4dev.demobooking.users.users_commands.domain.core.values;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@ToString(of = {"firstName", "lastName"})
@Getter
public final class FullName implements Serializable {

    private final FirstName firstName;
    private final LastName lastName;

    public FullName(FirstName firstName, LastName lastName) throws LastNameCantBeNullException, FirstNameCantBeNullException {
        Assert.notNull(firstName, FirstNameCantBeNullException::new);
        Assert.notNull(lastName, LastNameCantBeNullException::new);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Exceptions

    public static class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }

        @Override
        public String getCode() {
            return "InvalidFullName";
        }
    }

    public static final class FirstNameCantBeNullException extends ValidationException {
        public FirstNameCantBeNullException() {
            super("The first name cannot be null");
        }

        @Override
        public String getCode() {
            return "FirstNameIsNull";
        }
    }

    public static final class LastNameCantBeNullException extends ValidationException {
        public LastNameCantBeNullException() {
            super("The last name cannot be null");
        }

        @Override
        public String getCode() {
            return "LastNameIsNull";
        }
    }
}
