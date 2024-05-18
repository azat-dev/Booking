package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.presentation.ControllerException;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

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

    @ExceptionHandler(GenerateUserPhotoUploadUrlHandler.Exception.class)
    public ResponseEntity<String> handleGenerateUserPhotoUploadUrlException(GenerateUserPhotoUploadUrlHandler.Exception e) {
        throw ControllerException.createError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    public ResponseEntity<GenerateUploadUserPhotoUrlResponse> generateUploadUserPhototUrl(
        @RequestBody GenerateUploadUserPhotoUrlRequest requestBody,
        JwtAuthenticationToken jwtAuthenticationToken
    ) throws UserId.WrongFormatException, GenerateUserPhotoUploadUrlHandler.Exception {

        final UserId userId = UserId.checkAndMakeFrom(jwtAuthenticationToken.getName());

        final var command = requestBody.toCommand(userId, timeProvider);
        final var result = generateUserPhotoUploadUrlHandler.handle(command);

        return ResponseEntity.ok(new GenerateUploadUserPhotoUrlResponse(
            new UploadedFileDataDTO(
                result.formData().url().toString(),
                result.formData().bucketName().toString(),
                result.formData().objectName().toString()
            ),
            result.formData().value())
        );
    }

    @Override
    public ResponseEntity<String> updateUserPhoto(
        JwtAuthenticationToken jwtAuthenticationToken,
        UpdateUserPhotoRequestBody requestBody
    ) throws UserId.WrongFormatException {

        final UserId userId = UserId.checkAndMakeFrom(jwtAuthenticationToken.getName());
        final var request = new UpdateUserPhotoRequest(userId, requestBody);
        final var command = request.toCommand();

        try {
            updateUserPhotoHandler.handle(command, eventIdGenerator.generate(), timeProvider.currentTime());
            return ResponseEntity.ok("OK");
        } catch (UpdateUserPhotoHandler.Exception e) {
            throw ControllerException.createError(HttpStatus.NOT_FOUND, e);
        }
    }
}