package com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.domain.event.EventIdGenerator;
import com.azat4dev.demobooking.common.utils.TimeProvider;
import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.users_commands.domain.core.commands.UploadNewUserPhoto;
import com.azat4dev.demobooking.users.users_commands.domain.core.values.files.InitialUserPhotoFileName;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.GenerateUserPhotoUploadUrlHandler;
import com.azat4dev.demobooking.users.users_commands.domain.handlers.users.UpdateUserPhotoHandler;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlRequest;
import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.GenerateUploadUserPhotoUrlResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
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

    public ResponseEntity<GenerateUploadUserPhotoUrlResponse> generateUploadUserPhototUrl(
        @RequestBody GenerateUploadUserPhotoUrlRequest requestBody,
        JwtAuthenticationToken jwtAuthenticationToken
    ) {

        final var userId = UserId.fromString(jwtAuthenticationToken.getName());
        final var command = requestBody.toCommand(userId, timeProvider);
        final var result = generateUserPhotoUploadUrlHandler.handle(command);

        return ResponseEntity.ok(new GenerateUploadUserPhotoUrlResponse(result.url().toString()));
    }

    @Override
    public ResponseEntity<String> uploadNewUserPhoto(MultipartFile photo, JwtAuthenticationToken jwtAuthenticationToken) {

        final var userId = UserId.fromString(jwtAuthenticationToken.getName());
        final var fileName = InitialUserPhotoFileName.checkAndMakeFrom(photo.getOriginalFilename());

        final var command = UploadNewUserPhoto.checkAndMakeFrom(
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