package com.azat4dev.booking.users.users_commands.application.config;

import com.azat4dev.booking.shared.domain.values.user.UserIdFactory;
import com.azat4dev.booking.users.users_commands.application.handlers.LoginByEmailHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.LoginByEmailHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.SignUpHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.SignUpHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.email.verification.CompleteEmailVerificationHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.email.verification.CompleteEmailVerificationHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.password.CompletePasswordResetHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.password.CompletePasswordResetHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.password.ResetPasswordByEmailHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.password.ResetPasswordByEmailHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.GenerateUserPhotoUploadUrlHandlerImpl;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.UpdateUserPhotoHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.UpdateUserPhotoHandlerImpl;
import com.azat4dev.booking.users.users_commands.domain.handlers.email.verification.VerifyEmailByToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.SendResetPasswordEmail;
import com.azat4dev.booking.users.users_commands.domain.handlers.password.reset.SetNewPasswordByToken;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.Users;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.GenerateUrlForUploadUserPhoto;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.photo.SetNewPhotoForUser;
import com.azat4dev.booking.users.users_commands.domain.interfaces.services.PasswordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandHandlersConfig {

    @Bean
    public SignUpHandler signUpHandler(
        UserIdFactory userIdFactory,
        PasswordService passwordService,
        Users users
    ) {
        return new SignUpHandlerImpl(
            userIdFactory,
            passwordService,
            users
        );
    }

    @Bean
    public CompleteEmailVerificationHandler completeEmailVerificationHandler(
        VerifyEmailByToken verifyEmailByToken
    ) {
        return new CompleteEmailVerificationHandlerImpl(
            verifyEmailByToken
        );
    }

    @Bean
    public ResetPasswordByEmailHandler resetPasswordByEmailHandler(
        SendResetPasswordEmail sendResetPasswordEmail
    ) {
        return new ResetPasswordByEmailHandlerImpl(sendResetPasswordEmail);
    }

    @Bean
    public CompletePasswordResetHandler completePasswordResetHandler(
        SetNewPasswordByToken setNewPasswordByToken,
        PasswordService passwordService
    ) {
        return new CompletePasswordResetHandlerImpl(setNewPasswordByToken, passwordService);
    }

    @Bean
    public GenerateUserPhotoUploadUrlHandler generateUserPhotoUploadUrlHandler(
        GenerateUrlForUploadUserPhoto generateUrlForUploadUserPhoto
    ) {
        return new GenerateUserPhotoUploadUrlHandlerImpl(
            generateUrlForUploadUserPhoto
        );
    }

    @Bean
    public LoginByEmailHandler loginByEmailHandler(
        PasswordService passwordService,
        Users users
    ) {
        return new LoginByEmailHandlerImpl(passwordService, users);
    }

    @Bean
    public UpdateUserPhotoHandler updateUserPhotoHandler(SetNewPhotoForUser setNewPhotoForUser) {
        return new UpdateUserPhotoHandlerImpl(setNewPhotoForUser);
    }
}
