package com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.core.UserId;

public interface GenerateResetPasswordLink {

    LinkForPasswordReset execute(UserId userId);
}
