package com.azat4dev.demobooking.users.domain.services;

import com.azat4dev.demobooking.users.domain.values.email.EmailBody;

public record VerificationEmailBuilderResult(String subject, EmailBody body) {
}