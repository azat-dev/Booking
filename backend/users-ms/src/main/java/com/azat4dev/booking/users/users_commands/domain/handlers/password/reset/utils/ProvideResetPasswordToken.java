package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

@FunctionalInterface
public interface ProvideResetPasswordToken {

    TokenForPasswordReset execute(UserId userId);
}