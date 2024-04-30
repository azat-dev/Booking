package com.azat4dev.demobooking.users.domain;

import com.azat4dev.demobooking.users.domain.entities.FirstName;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.entities.LastName;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;

public class UserHelpers {
    public static EmailAddress anyValidEmail() {
        try {
            return EmailAddress.makeFromString("user@examples.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static FullName anyFullName() {
        try {
            return new FullName(
                new FirstName("John"),
                new LastName("Doe")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}