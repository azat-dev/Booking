package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities;

import jakarta.validation.constraints.NotNull;

public record SignUpResponse(
    @NotNull String userId,
    @NotNull GeneratedTokensDTO tokens
) {

}