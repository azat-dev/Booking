package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.UserId;

import java.util.Date;

public interface VerificationTokensService {

    VerificationToken makeVerificationToken(UserId userId, Date expirationDate);

    UserId parse(VerificationToken token) throws ExpiredVerificationToken, WrongFormatOfVerificationToken;
}
