package com.azat4dev.booking.users.users_commands.domain.core.entities;

import com.azat4dev.booking.shared.domain.DomainException;
import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.shared.utils.Assert;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.EncodedPassword;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.EmailVerificationStatus;
import com.azat4dev.booking.users.users_commands.domain.core.values.user.FullName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;


@EqualsAndHashCode(of = "id")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "encodedPassword")
public final class User {
    private final UserId id;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EmailAddress email;
    private FullName fullName;
    private EncodedPassword encodedPassword;
    private EmailVerificationStatus emailVerificationStatus;
    private Optional<UserPhotoPath> photo;

    public static User dangerouslyMakeFrom(
        UserId id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EmailAddress email,
        FullName fullName,
        EncodedPassword encodedPassword,
        EmailVerificationStatus emailVerificationStatus,
        Optional<UserPhotoPath> photo
    ) {
        return new User(id, createdAt, updatedAt, email, fullName, encodedPassword, emailVerificationStatus, photo);
    }

    public static User checkAndMake(
        UserId id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EmailAddress email,
        FullName fullName,
        EncodedPassword encodedPassword,
        Optional<UserPhotoPath> photo
    ) throws Exception {

        Assert.notNull(id, Exception.UserIdIsRequired::new);
        Assert.notNull(email, Exception.EmailIsRequired::new);
        Assert.notNull(fullName, Exception.FullNameIsRequired::new);
        Assert.notNull(encodedPassword, Exception.PasswordIsRequired::new);

        return new User(id, createdAt, updatedAt, email, fullName, encodedPassword, EmailVerificationStatus.NOT_VERIFIED, photo);
    }

    private void updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setEmail(EmailAddress newEmail) throws Exception.EmailIsRequired {
        Assert.notNull(email, Exception.EmailIsRequired::new);

        if (newEmail != this.email) {
            updateUpdatedAt();
        }

        this.emailVerificationStatus = EmailVerificationStatus.NOT_VERIFIED;
        this.email = newEmail;
    }

    public void setFullName(FullName newFullName) throws Exception.FullNameIsRequired {
        Assert.notNull(fullName, Exception.FullNameIsRequired::new);


        if (newFullName != this.fullName) {
            updateUpdatedAt();
        }

        this.fullName = newFullName;
    }

    public void setEncodedPassword(EncodedPassword newEncodedPassword) throws Exception.PasswordIsRequired {
        Assert.notNull(encodedPassword, Exception.PasswordIsRequired::new);

        if (newEncodedPassword != this.encodedPassword) {
            updateUpdatedAt();
        }

        this.encodedPassword = newEncodedPassword;
    }

    public EmailVerificationStatus emailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmailVerificationStatus(EmailVerificationStatus newEmailVerificationStatus) throws Exception.EmailVerificationStatusIsRequired {
        Assert.notNull(emailVerificationStatus, Exception.EmailVerificationStatusIsRequired::new);

        if (newEmailVerificationStatus != this.emailVerificationStatus) {
            updateUpdatedAt();
        }
        this.emailVerificationStatus = newEmailVerificationStatus;
    }

    public void setPhoto(UserPhotoPath photoPath) {
        if (photo.isEmpty() || photoPath.equals(photo.get())) {
            updateUpdatedAt();
        }
        this.photo = Optional.of(photoPath);
    }

    public void verifyEmail(EmailAddress verifiedEmail) throws Exception.VerifiedEmailDoesntExist {
        if (!email.equals(verifiedEmail)) {
            throw new Exception.VerifiedEmailDoesntExist();
        }
        this.emailVerificationStatus = EmailVerificationStatus.VERIFIED;
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

        public static final class VerifiedEmailDoesntExist extends Exception {
            public VerifiedEmailDoesntExist() {
                super("Verified email doesn't exist");
            }
        }
    }
}
