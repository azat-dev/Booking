package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.users.users_commands.application.commands.photo.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.users_commands.application.commands.photo.UpdateUserPhoto;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.booking.users.users_commands.application.handlers.photo.UpdateUserPhotoHandler;
import com.azat4dev.booking.usersms.generated.server.api.CommandsUpdateUserPhotoApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UpdateUserPhotoApi implements CommandsUpdateUserPhotoApiDelegate {

    @Autowired
    private UpdateUserPhotoHandler updateUserPhoto;

    @Autowired
    private GenerateUserPhotoUploadUrlHandler generateUserPhotoUploadUrlHandler;

    @Autowired
    private CurrentAuthenticatedUserIdProvider getCurrentUserId;

    @Override
    public ResponseEntity<GenerateUploadUserPhotoUrlResponseBodyDTO> generateUploadUserPhotoUrl(GenerateUploadUserPhotoUrlRequestBodyDTO requestBody) throws Exception {

        final var userId = getCurrentUserId.get().orElseThrow(() -> new ControllerException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        final var command = new GenerateUserPhotoUploadUrl(
            requestBody.getOperationId().toString(),
            userId.toString(),
            requestBody.getFileExtension(),
            requestBody.getFileSize()
        );


        try {
            final var result = generateUserPhotoUploadUrlHandler.handle(command);

            return ResponseEntity.ok(new GenerateUploadUserPhotoUrlResponseBodyDTO(
                UploadedFileDataDTO.builder()
                    .url(result.formData().url().toString())
                    .bucketName(result.formData().bucketName().toString())
                    .objectName(result.formData().objectName().toString())
                    .build(),
                result.formData().formData())
            );

        } catch (GenerateUserPhotoUploadUrlHandler.Exception.FailedGenerateUserPhotoUploadUrl e) {
            throw ControllerException.createError(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public ResponseEntity<UpdateUserPhoto200ResponseDTO> updateUserPhoto(UpdateUserPhotoRequestBodyDTO requestBody) throws Exception {

        final var userId = getCurrentUserId.get().orElseThrow(() -> new ControllerException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        final var command = new UpdateUserPhoto(
            userId,
            requestBody.getOperationId().toString(),
            new UpdateUserPhoto.UploadedFileData(
                requestBody.getUploadedFile().getBucketName(),
                requestBody.getUploadedFile().getObjectName()
            )
        );

        try {
            updateUserPhoto.handle(command);
            return ResponseEntity.ok(UpdateUserPhoto200ResponseDTO.builder().build());
        } catch (UpdateUserPhotoHandler.Exception.FailedToAttachPhoto e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}