package com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;

public interface GenerateResetPasswordLink {

    LinkForPasswordReset execute(UserId userId);
}
