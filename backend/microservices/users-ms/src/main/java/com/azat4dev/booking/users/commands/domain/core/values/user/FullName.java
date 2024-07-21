package com.azat4dev.booking.users.commands.domain.core.values.user;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.utils.Assert;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public final class FullName implements Serializable {

    private final FirstName firstName;
    private final LastName lastName;

    public static FullName makeWithChecks(FirstName firstName, LastName lastName) throws Exception {
        Assert.notNull(firstName, Exception.FirstNameCantBeNull::new);
        Assert.notNull(lastName, Exception.LastNameCantBeNull::new);

        return new FullName(
            firstName,
            lastName
        );
    }

    public static FullName makeWithoutChecks(FirstName firstName, LastName lastName) {
        return new FullName(firstName, lastName);
    }

    @Override
    public String toString() {
        return firstName.toString() + " " + lastName.toString();
    }

    // Exceptions

    public abstract static sealed class Exception extends DomainException permits Exception.LastNameCantBeNull, Exception.FirstNameCantBeNull {
        public Exception(String message) {
            super(message);
        }

        public static final class FirstNameCantBeNull extends Exception {
            public FirstNameCantBeNull() {
                super("The first name cannot be null");
            }
        }

        public static final class LastNameCantBeNull extends Exception {
            public LastNameCantBeNull() {
                super("The last name cannot be null");
            }
        }
    }
}
