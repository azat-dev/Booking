package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.domain.core.UserId;
import com.azat4dev.booking.shared.domain.event.EventIdGenerator;
import com.azat4dev.booking.shared.utils.TimeProvider;
import com.azat4dev.booking.users.users_commands.application.commands.photo.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.booking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.entities.*;
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
    ) throws GenerateUserPhotoUploadUrlHandler.Exception {

        final var command = new GenerateUserPhotoUploadUrl(
            requestBody.idempotentOperationId(),
            jwtAuthenticationToken.getName(),
            requestBody.fileExtension(),
            requestBody.fileSize()
        );

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
        @RequestBody UpdateUserPhotoRequestBody requestBody
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