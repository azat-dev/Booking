package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.UserId;

public interface VerificationTokensService {

    VerificationToken makeVerificationToken(UserId userId);

    UserId parse(VerificationToken token) throws ExpiredVerificationToken, WrongFormatOfVerificationToken;
}
