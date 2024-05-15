package com.azat4dev.demobooking.users.users_commands.domain.services.password.reset;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.password.reset.TokenForPasswordReset;

@FunctionalInterface
public interface ProvideResetPasswordToken {

    TokenForPasswordReset execute(UserId userId);
}
