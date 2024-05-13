package com.azat4dev.demobooking.users.users_commands.domain.interfaces.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.email.EmailAddress;

public interface EmailVerificationTokensService {

    EmailVerificationToken generateFor(UserId userId, EmailAddress emailAddress);

    void verify(EmailVerificationToken token) throws Exception;
}
