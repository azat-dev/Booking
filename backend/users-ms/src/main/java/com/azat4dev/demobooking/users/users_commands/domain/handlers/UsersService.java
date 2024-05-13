package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CompleteEmailVerification;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.CreateUser;

public interface UsersService {

    void handle(CreateUser command) throws UserWithSameEmailAlreadyExistsException;

    void handle(CompleteEmailVerification command) throws InvalidEmailVerificationTokenException, EmailVerificationTokenExpiredException;

    // Exceptions

    final class UserWithSameEmailAlreadyExistsException extends DomainException {
        public UserWithSameEmailAlreadyExistsException() {
            super("User with same email already exists");
        }

        @Override
        public String getCode() {
            return "UserWithSameEmailAlreadyExists";
        }
    }

    final class InvalidEmailVerificationTokenException extends DomainException {
        public InvalidEmailVerificationTokenException() {
            super("Email verification token not found");
        }

        @Override
        public String getCode() {
            return "InvalidEmailVerificationToken";
        }
    }

    final class EmailVerificationTokenExpiredException extends DomainException {
        public EmailVerificationTokenExpiredException() {
            super("Email verification token expired");
        }

        @Override
        public String getCode() {
            return "EmailVerificationTokenExpired";
        }
    }
}