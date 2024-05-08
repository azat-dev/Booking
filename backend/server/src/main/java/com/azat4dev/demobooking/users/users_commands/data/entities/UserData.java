package com.azat4dev.demobooking.users.users_commands.data.entities;

import com.azat4dev.demobooking.users.users_commands.domain.services.EmailVerificationStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserData {

    private @Id UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private @Nonnull String email;

    @Column(name = "encoded_password", nullable = false)
    private String encodedPassword;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private EmailVerificationStatus emailVerificationStatus;


    public UserData(
        UUID id,
        @Nonnull LocalDateTime createdAt,
        @Nonnull LocalDateTime updatedAt,
        @Nonnull String email,
        @Nonnull String firstName,
        @Nonnull String lastName,
        @Nonnull String encodedPassword,
        @Nonnull EmailVerificationStatus emailVerificationStatus
    ) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.encodedPassword = encodedPassword;
        this.emailVerificationStatus = emailVerificationStatus;
    }
}
