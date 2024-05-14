package com.azat4dev.demobooking.users.users_commands.domain.core.commands;

import com.azat4dev.demobooking.common.domain.event.DomainEventPayload;
import com.azat4dev.demobooking.users.users_commands.domain.interfaces.services.EmailVerificationToken;


public record CompleteEmailVerification(EmailVerificationToken token) implements DomainEventPayload {
}