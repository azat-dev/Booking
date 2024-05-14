package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.Command;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public record ResetPasswordByEmail(
    String idempotentOperationToken,
    EmailAddress email
) implements Command {
}
