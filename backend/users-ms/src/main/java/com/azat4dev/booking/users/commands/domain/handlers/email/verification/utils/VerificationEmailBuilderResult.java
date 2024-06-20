package com.azat4dev.booking.users.commands.domain.handlers.email.verification.utils;

import com.azat4dev.booking.users.commands.domain.core.values.email.EmailBody;

public record VerificationEmailBuilderResult(String subject, EmailBody body) {
}