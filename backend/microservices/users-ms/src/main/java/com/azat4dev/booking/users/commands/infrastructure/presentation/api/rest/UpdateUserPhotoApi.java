package com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest;

import com.azat4dev.booking.shared.application.ControllerException;
import com.azat4dev.booking.shared.presentation.CurrentAuthenticatedUserIdProvider;
import com.azat4dev.booking.users.commands.application.commands.photo.GenerateUserPhotoUploadUrl;
import com.azat4dev.booking.users.commands.application.commands.photo.UpdateUserPhoto;
import com.azat4dev.booking.users.commands.application.handlers.photo.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.booking.users.commands.application.handlers.photo.UpdateUserPhotoHandler;
import com.azat4dev.booking.usersms.generated.server.api.CommandsUpdateUserPhotoApiDelegate;
import com.azat4dev.booking.usersms.generated.server.model.*;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Observed
@Component
public class UpdateUserPhotoApi implements CommandsUpdateUserPhotoApiDelegate {

    private final UpdateUserPhotoHandler updateUserPhoto;
    private final GenerateUserPhotoUploadUrlHandler generateUserPhotoUploadUrlHandler;
    private final CurrentAuthenticatedUserIdProvider getCurrentUserId;

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

            final var response = ResponseEntity.ok(new GenerateUploadUserPhotoUrlResponseBodyDTO(
                UploadedFileDataDTO.builder()
                    .url(result.formData().url().toString())
                    .bucketName(result.formData().bucketName().toString())
                    .objectName(result.formData().objectName().toString())
                    .build(),
                result.formData().formData())
            );

            log.atInfo().addKeyValue("userId", userId).log("User photo upload url generated");
            return response;

        } catch (GenerateUserPhotoUploadUrlHandler.Exception.FailedGenerateUserPhotoUploadUrl e) {
            log.atError().setCause(e).log("Failed to generate user photo upload url");
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
            final var response = ResponseEntity.ok(UpdateUserPhoto200ResponseDTO.builder().build());
            log.atInfo().addKeyValue("userId", userId).log("User photo updated");
            return response;
        } catch (UpdateUserPhotoHandler.Exception.FailedToAttachPhoto e) {
            log.atError().setCause(e).log("Failed to attach photo");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}