package com.azat4dev.booking.users.config.users_commands.domain;

import com.azat4dev.booking.shared.domain.interfaces.bus.DomainEventsBus;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtDataDecoder;
import com.azat4dev.booking.users.config.users_commands.properties.ResetPasswordConfigProperties;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SendResetPasswordEmail;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SendResetPasswordEmailImpl;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SetNewPasswordByToken;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.SetNewPasswordByTokenImpl;
import com.azat4dev.booking.users.commands.domain.handlers.password.reset.utils.*;
import com.azat4dev.booking.users.commands.domain.interfaces.repositories.UsersRepository;
import com.azat4dev.booking.users.commands.domain.interfaces.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ResetPasswordConfig {

    private final EmailService emailService;
    private final UsersRepository usersRepository;
    private final DomainEventsBus bus;
    private final TimeProvider timeProvider;
    private final JwtDataDecoder decoder;
    private final ResetPasswordConfigProperties resetPasswordConfigProperties;

    @Bean
    public SendResetPasswordEmail sendResetPasswordEmail(BuildResetPasswordEmail buildResetPasswordEmail) {

        return new SendResetPasswordEmailImpl(
            usersRepository,
            buildResetPasswordEmail,
            emailService,
            bus
        );
    }

    @Bean
    public SetNewPasswordByToken resetPasswordByToken(ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId) {
        return new SetNewPasswordByTokenImpl(
            validateTokenForPasswordResetAndGetUserId,
            usersRepository,
            bus
        );
    }

    @Bean
    public BuildResetPasswordEmail buildResetPasswordEmail(
        GenerateResetPasswordLink generateResetPasswordLink
    ) {
        return new BuildResetPasswordEmailImpl(
            resetPasswordConfigProperties.getSendEmailFromName(),
            resetPasswordConfigProperties.getSendEmailFromAddress(),
            resetPasswordConfigProperties.getSendEmailSubject(),
            generateResetPasswordLink
        );
    }

    @Bean
    public GenerateResetPasswordLink generateResetPasswordLink(ProvideResetPasswordToken provideResetPasswordToken) {
        return new GenerateResetPasswordLinkImpl(
            resetPasswordConfigProperties.getBaseUrlForLink(),
            provideResetPasswordToken
        );
    }

    @Bean
    ValidateTokenForPasswordResetAndGetUserId validateTokenForPasswordResetAndGetUserId(
        GetInfoForPasswordResetToken getTokenInfo
    ) {
        return new ValidateTokenForPasswordResetAndGetUserIdImpl(
            getTokenInfo,
            timeProvider
        );
    }

    @Bean
    GetInfoForPasswordResetToken getInfoForPasswordResetToken() {
        return new GetInfoForPasswordResetTokenImpl(decoder);
    }
}
