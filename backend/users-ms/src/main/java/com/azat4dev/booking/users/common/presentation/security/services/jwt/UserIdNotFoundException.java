package com.azat4dev.booking.users.common.presentation.security.services.jwt;

import org.springframework.security.core.AuthenticationException;

public class UserIdNotFoundException extends AuthenticationException {

    public UserIdNotFoundException(String msg) {
        super(msg);
    }

    public UserIdNotFoundException(
        String msg,
        Throwable cause
    ) {
        super(
            msg,
            cause
        );
    }
}