package com.azat4dev.demobooking.users.users_commands.domain.core.entities;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.utils.Assert;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.user.FullName;
import lombok.*;

import java.time.LocalDateTime;


@EqualsAndHashCode(of = "id")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "encodedPassword")
public final class User {
    private final UserId id;
    private final LocalDateTime createdAt;
    private EmailAddress email;
    private FullName fullName;
    private EncodedPassword encodedPassword;
    private EmailVerificationStatus emailVerificationStatus;

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

    public static User checkAndMake(
        UserId id,
        LocalDateTime createdAt,
        EmailAddress email,
        FullName fullName,
        EncodedPassword encodedPassword
    ) throws Exception {

        Assert.notNull(id, Exception.UserIdIsRequired::new);
        Assert.notNull(email, Exception.EmailIsRequired::new);
        Assert.notNull(fullName, Exception.FullNameIsRequired::new);
        Assert.notNull(encodedPassword, Exception.PasswordIsRequired::new);

        return new User(id, createdAt, email, fullName, encodedPassword, EmailVerificationStatus.NOT_VERIFIED);
    }

    public void setEmail(EmailAddress newEmail) throws Exception.EmailIsRequired {
        Assert.notNull(email, Exception.EmailIsRequired::new);
        this.emailVerificationStatus = EmailVerificationStatus.NOT_VERIFIED;
        this.email = newEmail;
    }

    public void setFullName(FullName newFullName) throws Exception.FullNameIsRequired {
        Assert.notNull(fullName, Exception.FullNameIsRequired::new);
        this.fullName = newFullName;
    }

    public void setEncodedPassword(EncodedPassword newEncodedPassword) throws Exception.PasswordIsRequired {
        Assert.notNull(encodedPassword, Exception.PasswordIsRequired::new);
        this.encodedPassword = newEncodedPassword;
    }

    public EmailVerificationStatus emailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(EmailVerificationStatus newEmailVerificationStatus) throws Exception.EmailVerificationStatusIsRequired {
        Assert.notNull(emailVerificationStatus, Exception.EmailVerificationStatusIsRequired::new);
        this.emailVerificationStatus = newEmailVerificationStatus;
    }

    public void setPhoto(UserPhotoPath photoPath) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Exceptions

    public static abstract class Exception extends DomainException {
        public Exception(String message) {
            super(message);
        }

        public static final class UserIdIsRequired extends Exception {
            public UserIdIsRequired() {
                super("User ID is required");
            }
        }

        public static final class EmailIsRequired extends Exception {
            public EmailIsRequired() {
                super("Email is required");
            }
        }

        public static final class FullNameIsRequired extends Exception {
            public FullNameIsRequired() {
                super("Full name is required");
            }
        }

        public static final class PasswordIsRequired extends Exception {
            public PasswordIsRequired() {
                super("Password is required");
            }
        }

        public static final class EmailVerificationStatusIsRequired extends Exception {
            public EmailVerificationStatusIsRequired() {
                super("Email verification status is required");
            }
        }
    }
}
