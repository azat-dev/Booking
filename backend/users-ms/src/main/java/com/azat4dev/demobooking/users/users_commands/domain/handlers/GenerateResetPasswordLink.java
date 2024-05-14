package com.azat4dev.demobooking.users.users_commands.domain.handlers;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public interface GenerateResetPasswordLink {

    ResetPasswordLink execute(UserId userId);
}
