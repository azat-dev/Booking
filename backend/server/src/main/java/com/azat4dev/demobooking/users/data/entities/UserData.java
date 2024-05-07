package com.azat4dev.demobooking.users.data.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserData {

    private @Id UUID id;
    private @Nonnull String email;
    private @Nonnull String encodedPassword;

    public UserData() {
    }

    public UserData(
        UUID id,
        @Nonnull String email,
        @Nonnull String encodedPassword
    ) {
        this.id = id;
        this.email = email;
        this.encodedPassword = encodedPassword;
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
