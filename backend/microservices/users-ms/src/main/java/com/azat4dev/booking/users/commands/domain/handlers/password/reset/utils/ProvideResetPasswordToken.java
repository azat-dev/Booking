package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.password.reset.TokenForPasswordReset;

@FunctionalInterface
public interface ProvideResetPasswordToken {

    TokenForPasswordReset execute(UserId userId);
}
