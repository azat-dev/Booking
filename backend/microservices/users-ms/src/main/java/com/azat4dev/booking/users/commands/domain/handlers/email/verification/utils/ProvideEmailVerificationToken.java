package com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.commands.domain.core.values.email.EmailAddress;
import com.azat4dev.booking.users.commands.domain.core.values.email.verification.EmailVerificationToken;

public interface ProvideEmailVerificationToken {

    EmailVerificationToken execute(UserId userId, EmailAddress emailAddress);
}
