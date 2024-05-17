package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.DomainException;
import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.presentation.ControllerException;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UploadNewUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.InitialUserPhotoFileName;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlRequest;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class CurrentUserController implements CurrentUserResource {

    @Autowired
    private UpdateUserPhotoHandler updateUserPhotoHandler;

    @Autowired
    private TimeProvider timeProvider;

    @Autowired
    private EventIdGenerator eventIdGenerator;

    @Autowired
    private GenerateUserPhotoUploadUrlHandler generateUserPhotoUploadUrlHandler;

    @ExceptionHandler({UserId.WrongFormatException.class})
    public ResponseEntity<String> handleWrongUserId() {
        throw ControllerException.createError(HttpStatus.UNAUTHORIZED, "invalidToken", "Invalid token");
    }

    @ExceptionHandler({InitialUserPhotoFileName.Exception.class, UploadNewUserPhoto.MaxSizeException.class})
    public ResponseEntity<String> handleException(DomainException e) {
        throw ControllerException.createError(HttpStatus.BAD_REQUEST, e);
    }

    public ResponseEntity<GenerateUploadUserPhotoUrlResponse> generateUploadUserPhototUrl(
        @RequestBody GenerateUploadUserPhotoUrlRequest requestBody,
        JwtAuthenticationToken jwtAuthenticationToken
    ) throws UserId.WrongFormatException {

        final UserId userId= UserId.checkAndMakeFrom(jwtAuthenticationToken.getName());

        final var command = requestBody.toCommand(userId, timeProvider);
        final var result = generateUserPhotoUploadUrlHandler.handle(command);

        return ResponseEntity.ok(new GenerateUploadUserPhotoUrlResponse(result.url().toString()));
    }

    @Override
    public ResponseEntity<String> uploadNewUserPhoto(MultipartFile photo, JwtAuthenticationToken jwtAuthenticationToken)
        throws UserId.WrongFormatException, InitialUserPhotoFileName.Exception,
            UploadNewUserPhoto.MaxSizeException, UpdateUserPhotoHandler.Exception {

        final UserId userId = UserId.checkAndMakeFrom(jwtAuthenticationToken.getName());
        final InitialUserPhotoFileName fileName = InitialUserPhotoFileName.checkAndMakeFrom(photo.getOriginalFilename());

        final UploadNewUserPhoto command = UploadNewUserPhoto.checkAndMakeFrom(
            userId,
            photo.getSize(),
            fileName,
            () -> {
                try {
                    return photo.getInputStream().readAllBytes();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );

        updateUserPhotoHandler.handle(
            command,
            eventIdGenerator.generate(),
            timeProvider.currentTime()
        );
        return ResponseEntity.ok("Photo updated");
    }
}