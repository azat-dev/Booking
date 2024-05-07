package com.azat4dev.demobooking.users.data.entities;

import com.azat4dev.demobooking.users.domain.services.EmailVerificationStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserData {

    private @Id UUID id;
    private @Nonnull String email;
    private @Nonnull String encodedPassword;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private EmailVerificationStatus emailVerificationStatus;

    public UserData() {
    }

    public UserData(
        UUID id,
        @Nonnull String email,
        @Nonnull String encodedPassword,
        @Nonnull EmailVerificationStatus emailVerificationStatus
    ) {
        this.id = id;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nonnull String email) {
        this.email = email;
    }

    @Nonnull
    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(@Nonnull String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData userData)) return false;
        return Objects.equals(getId(), userData.getId()) && Objects.equals(getEmail(), userData.getEmail()) && Objects.equals(getEncodedPassword(), userData.getEncodedPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getEncodedPassword());
    }
}
