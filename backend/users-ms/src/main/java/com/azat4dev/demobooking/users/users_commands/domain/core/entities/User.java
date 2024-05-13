package com.azat4dev.demobooking.users.users_commands.domain.core.entities;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@EqualsAndHashCode(of = "id")
public final class User {
    private final UserId id;
    private final LocalDateTime createdAt;
    private EmailAddress email;
    private FullName fullName;
    private EncodedPassword encodedPassword;
    private EmailVerificationStatus emailVerificationStatus;

    private User(
        UserId id,
        LocalDateTime createdAt,
        EmailAddress email,
        FullName fullName,
        EncodedPassword encodedPassword,
        EmailVerificationStatus emailVerificationStatus
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.email = email;
        this.fullName = fullName;
        this.encodedPassword = encodedPassword;
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public static User dangerouslyMakeFrom(
        UserId id,
        LocalDateTime createdAt,
        EmailAddress email,
        FullName fullName,
        EncodedPassword encodedPassword,
        EmailVerificationStatus emailVerificationStatus
    ) {
        return new User(id, createdAt, email, fullName, encodedPassword, emailVerificationStatus);
    }

    public static User makeNew(
        UserId id,
        LocalDateTime createdAt,
        EmailAddress email,
        FullName fullName,
        EncodedPassword encodedPassword
    ) throws EmailIsRequired, FullNameIsRequired, PasswordIsRequired {

        Assert.notNull(id, UserIdIsRequired::new);
        Assert.notNull(email, EmailIsRequired::new);
        Assert.notNull(fullName, FullNameIsRequired::new);
        Assert.notNull(encodedPassword, PasswordIsRequired::new);

        return new User(id, createdAt, email, fullName, encodedPassword, EmailVerificationStatus.NOT_VERIFIED);
    }

    public UserId getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public void setEmail(EmailAddress newEmail) {
        Assert.notNull(email, EmailIsRequired::new);
        this.email = newEmail;
    }

    public FullName getFullName() {
        return fullName;
    }

    public void setFullName(FullName newFullName) {
        Assert.notNull(fullName, FullNameIsRequired::new);
        this.fullName = newFullName;
    }

    public EncodedPassword getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(EncodedPassword newEncodedPassword) {
        Assert.notNull(encodedPassword, PasswordIsRequired::new);
        this.encodedPassword = newEncodedPassword;
    }

    public EmailVerificationStatus emailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(EmailVerificationStatus newEmailVerificationStatus) {
        Assert.notNull(emailVerificationStatus, EmailVerificationStatusIsRequired::new);
        this.emailVerificationStatus = newEmailVerificationStatus;
    }

    @Override
    public String toString() {
        return "User[" +
               "id=" + id + ", " +
               "createdAt=" + createdAt + ", " +
               "email=" + email + ", " +
               "fullName=" + fullName + ", " +
               "emailVerificationStatus=" + emailVerificationStatus + ']';
    }

    // Exceptions

    public static abstract class ValidationException extends DomainException {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static final class UserIdIsRequired extends ValidationException {
        public UserIdIsRequired() {
            super("User ID is required");
        }

        @Override
        public String getCode() {
            return "UserIdIsRequired";
        }
    }

    public static final class EmailIsRequired extends ValidationException {
        public EmailIsRequired() {
            super("Email is required");
        }

        @Override
        public String getCode() {
            return "EmailIsRequired";
        }
    }

    public static final class FullNameIsRequired extends ValidationException {
        public FullNameIsRequired() {
            super("Full name is required");
        }

        @Override
        public String getCode() {
            return "FullNameIsRequired";
        }
    }

    public static final class PasswordIsRequired extends ValidationException {
        public PasswordIsRequired() {
            super("Password is required");
        }

        @Override
        public String getCode() {
            return "PasswordIsRequired";
        }
    }

    public static final class EmailVerificationStatusIsRequired extends ValidationException {
        public EmailVerificationStatusIsRequired() {
            super("Email verification status is required");
        }

        @Override
        public String getCode() {
            return "EmailVerificationStatusIsRequired";
        }
    }
}
