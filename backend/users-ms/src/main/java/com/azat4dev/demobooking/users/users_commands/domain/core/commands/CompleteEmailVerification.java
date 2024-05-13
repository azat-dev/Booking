package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class CompleteEmailVerification implements DomainEventPayload {
    private final EmailVerificationToken token;

}