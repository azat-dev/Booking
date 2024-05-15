package com.azat4dev.demobooking.users.users_commands.domain.interfaces.functions;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.PasswordResetToken;

@FunctionalInterface
public interface ProvideResetPasswordToken {

    PasswordResetToken execute(UserId userId);
}
