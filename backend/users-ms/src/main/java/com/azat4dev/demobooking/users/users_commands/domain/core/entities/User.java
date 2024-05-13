package com.azat4dev.demobooking.users.users_commands.domain.core.entities;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.EmailVerificationStatus;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.FullName;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EncodedPassword;

import java.time.LocalDateTime;
import java.util.Objects;


public final class User {
    private final UserId id;
    private final LocalDateTime createdAt;
    private EmailAddress email;
    private FullName fullName;
    private EncodedPassword encodedPassword;
    private EmailVerificationStatus emailVerificationStatus;

    public User(
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

    public UserId id() {
        return id;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public EmailAddress email() {
        return email;
    }

    public FullName fullName() {
        return fullName;
    }

    public EncodedPassword encodedPassword() {
        return encodedPassword;
    }

    public EmailVerificationStatus emailVerificationStatus() {
        return emailVerificationStatus;
    }

    public void setEmail(EmailAddress newEmail) {
        this.email = newEmail;
    }

    public void setEmailVerificationStatus(EmailVerificationStatus newStatus) {
        this.emailVerificationStatus = newStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id.equals(user.id);
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
}
