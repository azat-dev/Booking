package com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record FullName(
    @NotBlank @Length(min = 1, max = 255) String firstName,
    @NotBlank @Length(min = 1, max = 255) String lastName
) {
}
