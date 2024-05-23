package com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.users_commands.domain.core.values.email.verification.EmailVerificationToken;

public interface ProvideEmailVerificationToken {

    EmailVerificationToken execute(UserId userId, EmailAddress emailAddress);
}
