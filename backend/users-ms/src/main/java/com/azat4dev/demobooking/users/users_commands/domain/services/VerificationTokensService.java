package com.azat4dev.demobooking.users.users_commands.domain.services;

import com.azat4dev.demobooking.users.common.domain.values.UserId;

public interface VerificationTokensService {

    VerificationToken makeVerificationToken(UserId userId);

    UserId parse(VerificationToken token) throws ExpiredVerificationToken, WrongFormatOfVerificationToken;
}
