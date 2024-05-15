package com.azat4dev.demobooking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public interface GenerateResetPasswordLink {

    LinkForPasswordReset execute(UserId userId);
}
